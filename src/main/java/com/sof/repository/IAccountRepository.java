package com.sof.repository;

import com.sof.model.Account;

import java.util.List;

public interface IAccountRepository extends IGenericRepository<Account, Long> {

    List<Account> findByPersonId(Long personId);
}
