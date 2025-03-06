package com.sof.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sof.dto.AccountDTO;
import com.sof.model.Account;
import com.sof.service.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAccountService accountService;
    private static final String USER = "Angelo";

    private AccountDTO accountDTO;
    private Account mockAccount;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        accountDTO = AccountDTO.builder()
                .accountId(1L)
                .accountNumber("12345678")
                .accountType("SAVINGS")
                .initialBalance(BigDecimal.valueOf(1000.0))
                .build();

        mockAccount = Account.builder()
                .accountId(1L)
                .accountNumber("12345678")
                .accountType("SAVINGS")
                .initialBalance(BigDecimal.valueOf(1000.0))
                .status(true)
                .personId(1L)
                .createdDate(LocalDateTime.now())
                .createdByUser("TestUser")
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfAccounts_WhenHasAdminRole() throws Exception {
        when(accountService.findAll()).thenReturn(List.of(mockAccount));

        mockMvc.perform(get("/api/v1/cuentas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountNumber").value("12345678"));

        verify(accountService, times(1)).findAll();
    }

    @Test
    void getAccountById_ShouldReturnAccount_WhenIdExists() throws Exception {
        when(accountService.findById(1L)).thenReturn(mockAccount);

        mockMvc.perform(get("/api/v1/cuentas/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345678"));

        verify(accountService, times(1)).findById(1L);
    }

    @Test
    void testSaveAccount() throws Exception {
        when(accountService.saveAccount(any(Account.class), eq(USER))).thenReturn(mockAccount);

        mockMvc.perform(post("/api/v1/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAccount)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/v1/cuentas/1"));

        verify(accountService, times(1)).saveAccount(any(Account.class), eq(USER));
    }

    @Test
    void testUpdateAccount() throws Exception {
        when(accountService.updateAccount(eq(1L), any(Account.class), eq(USER))).thenReturn(mockAccount);

        mockMvc.perform(put("/api/v1/cuentas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.accountNumber", is("12345678")))
                .andExpect(jsonPath("$.status", is(true)));

        verify(accountService, times(1)).updateAccount(eq(1L), any(Account.class), eq(USER));
    }

    @Test
    //@WithMockUser
    void testDeleteAccount() throws Exception {
        mockMvc.perform(delete("/api/v1/cuentas/1"))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteLogic(1L, USER);
    }
}