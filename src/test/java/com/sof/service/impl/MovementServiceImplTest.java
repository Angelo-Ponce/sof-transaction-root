package com.sof.service.impl;

import com.sof.exception.ModelNotFoundException;
import com.sof.model.Account;
import com.sof.model.Movement;
import com.sof.repository.IMovementRepository;
import com.sof.service.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private IMovementRepository repository;

    @Mock
    private IAccountService accountService;

    @InjectMocks
    private MovementServiceImpl service;

    private final String user = "Angelo";

    private final Long movementId = 1L;
    private Movement mockMovement;

    @BeforeEach
    void setUp() {
        mockMovement = new Movement();
        mockMovement.setAccountId(1L);
        mockMovement.setMovementValue(BigDecimal.valueOf(500));
    }

    @Test
    void givenSaveMove_WhenMoveIsValid_ThenSaveSuccessfully() {
        Account accountEntity = new Account();
        accountEntity.setAccountId(1L);
        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(1L)).thenReturn(accountEntity);
        when(accountService.saveAccount(any(Account.class), eq(user))).thenReturn(accountEntity);
        when(repository.save(any(Movement.class))).thenReturn(new Movement());

        service.saveMovement(mockMovement, user);

        assertEquals(accountEntity.getInitialBalance(), BigDecimal.valueOf(1500));
        verify(accountService, times(1)).findById(1L);
        verify(accountService, times(1)).saveAccount(any(Account.class), eq(user));
        verify(repository, times(1)).save(any(Movement.class));
    }

    @Test
    void givenSaveMove_WhenMoveIsNegative_ThenSaveSuccessfully() {
        mockMovement.setMovementValue(BigDecimal.valueOf(-500)); // Negative movement

        Account accountEntity = new Account();
        accountEntity.setAccountId(1L);
        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(1L)).thenReturn(accountEntity);
        when(accountService.saveAccount(any(Account.class), eq(user))).thenReturn(accountEntity);
        when(repository.save(any(Movement.class))).thenReturn(new Movement());

        service.saveMovement(mockMovement, user);

        verify(accountService, times(1)).findById(1L);
        verify(accountService, times(1)).saveAccount(any(Account.class), eq(user));
        verify(repository, times(1)).save(any(Movement.class));
    }

    @Test
    void givenSaveMovement_WhenTheAmountExceeds_ThenThrowExceptionForInvalidMovement() {
        mockMovement.setMovementValue(BigDecimal.valueOf(-2000)); // Exceeds balance

        Account accountEntity = new Account();
        accountEntity.setAccountId(1L);
        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(1L)).thenReturn(accountEntity);

        assertThrows(ModelNotFoundException.class, () -> service.saveMovement(mockMovement, eq(user)));

        verify(accountService, times(1)).findById(1L);
        verify(accountService, times(0)).saveAccount(any(Account.class), eq(user));
        verify(repository, times(0)).save(any(Movement.class));
    }

    @Test
    void givenUpdateEntity_WhenEntityExists_ThenReturnUpdatedEntity(){
        when(repository.findById(movementId)).thenReturn(Optional.of(mockMovement));
        when(repository.save(any(Movement.class))).thenReturn(mockMovement);
        Movement result = service.updateMovement(movementId, mockMovement, user);
        assertNotNull(result);
        verify(repository, times(1)).findById(movementId);
        verify(repository, times(1)).save(any());
    }

    @Test
    void givenDeleteLogic_WhenMovementExists_ThenMarkMovementAsInactive() {
        when(repository.findById(movementId)).thenReturn(Optional.of(mockMovement));
        when(repository.save(mockMovement)).thenReturn(mockMovement);

        service.deleteLogic(movementId, user);

        verify(repository, times(1)).findById(movementId);
        verify(repository, times(1)).save(mockMovement);
        assertEquals(Boolean.FALSE, mockMovement.getStatus());
        assertEquals(user, mockMovement.getLastModifiedByUser());
    }

    @Test
    void givenDeleteLogic_WhenMovementDoesNotExist_ThenThrowModelNotFoundException() {
        when(repository.findById(movementId)).thenReturn(Optional.empty());
        assertThrows(ModelNotFoundException.class, () -> service.deleteLogic(movementId, user));
        verify(repository, times(1)).findById(movementId);
        verify(repository, times(0)).save(any(Movement.class));
    }
}