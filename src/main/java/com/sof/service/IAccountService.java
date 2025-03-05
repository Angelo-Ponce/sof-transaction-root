package com.sof.service;

import com.sof.model.Account;

import java.util.List;

public interface IAccountService extends ICRUDService<Account, Long> {

    List<Account> findByPersonId(Long personId);
    Account saveAccount(Account account, String user);
    Account updateAccount(Long id, Account account, String user);
    void deleteLogic(Long id, String user);

}
