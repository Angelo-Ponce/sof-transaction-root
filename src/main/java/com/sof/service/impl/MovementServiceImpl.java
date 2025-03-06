package com.sof.service.impl;

import com.sof.dto.ClientDTO;
import com.sof.dto.MovementReportDTO;
import com.sof.exception.ModelNotFoundException;
import com.sof.model.Account;
import com.sof.model.Movement;
import com.sof.repository.IGenericRepository;
import com.sof.repository.IMovementRepository;
import com.sof.service.IAccountService;
import com.sof.service.IMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.sof.constants.Constants.DEPOSIT;
import static com.sof.constants.Constants.WITHDRAWAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl extends CRUDServiceImpl<Movement, Long> implements IMovementService {

    private final IMovementRepository repository;

    private final IAccountService accountService;

    private final ClientService clientService;

    @Override
    protected IGenericRepository<Movement, Long> getRepository() {
        return repository;
    }

    @Override
    public Movement saveMovement(Movement movement, String user) {

        Account account = this.accountService.findById(movement.getAccountId());
        BigDecimal balance = account.getInitialBalance().add(movement.getMovementValue());
        validMovement(movement, balance);

        // Actualizar detalles del movimiento y la cuenta
        updateMovementDetails(movement, account, balance, user);

        this.accountService.saveAccount(account, user);
        return repository.save(movement);
    }

    @Override
    public Movement updateMovement(Long id, Movement movement, String user) {
        return repository.findById(id)
                .map(existingMovement -> {
                    existingMovement.setAccountId(movement.getAccountId());
                    existingMovement.setMovementDate(movement.getMovementDate());
                    existingMovement.setMovementType(movement.getMovementType());
                    existingMovement.setMovementValue(movement.getMovementValue());
                    existingMovement.setBalance(movement.getBalance());
                    existingMovement.setStatus(movement.getStatus());
                    existingMovement.setLastModifiedByUser(user);
                    return repository.save(existingMovement);
                }).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
    }

    @Override
    public void deleteLogic(Long id, String user) {
        Movement movement = this.findById(id);
        movement.setStatus(false);
        movement.setLastModifiedByUser(user);
        repository.save(movement);
    }

    @Override
    public List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate) {
        ClientDTO client = clientService.findClientById(clientId);
        if (client == null) {
            throw new ModelNotFoundException("Client not found");
        }
        List<Movement> movementList = repository.findMovementByAccount_PersonIdAndMovementDateBetween(client.getPersonId(), startDate, endDate);
        return movementList.stream()
                .map(movement -> MovementReportDTO.builder()
                        .movementDate(movement.getMovementDate())
                        .name(client.getName())
                        .accountNumber(movement.getAccount().getAccountNumber())
                        .accountType(movement.getAccount().getAccountType())
                        .initialBalance(movement.getBalance())
                        .movementStatus(movement.getStatus())
                        .balance(movement.getAccount().getInitialBalance())
                        .build())
                .collect(Collectors.toList());

    }

    private void validMovement(Movement movement, BigDecimal balance){
        if(balance.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new ModelNotFoundException("Saldo no disponible");
        }
        if (movement.getMovementValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new ModelNotFoundException("Movimiento no valido");
        }
    }

    private void updateMovementDetails(Movement movement, Account account, BigDecimal newBalance, String user) {
        movement.setMovementType(determineMovementType(movement.getMovementValue()));
        movement.setAccountId(account.getAccountId());
        movement.setMovementDate(LocalDateTime.now());
        movement.setBalance(account.getInitialBalance());
        movement.setStatus(Boolean.TRUE);
        movement.setCreatedByUser(user);

        account.setInitialBalance(newBalance);
    }

    private String determineMovementType(BigDecimal movementValue) {
        return movementValue.compareTo(BigDecimal.ZERO) > 0
                ? DEPOSIT
                : WITHDRAWAL;
    }
}
