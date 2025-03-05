package com.sof.service.impl;

import com.sof.exception.ModelNotFoundException;
import com.sof.model.Account;
import com.sof.repository.IAccountRepository;
import com.sof.repository.IGenericRepository;
import com.sof.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends CRUDServiceImpl<Account, Long> implements IAccountService {

    private final IAccountRepository repository;

    @Override
    protected IGenericRepository<Account, Long> getRepository() {
        return repository;
    }

    @Override
    public List<Account> findByPersonId(Long personId) {
        return repository.findByPersonId(personId);
    }

    @Override
    public Account saveAccount(Account account, String user) {
        account.setCreatedByUser(user);
        return repository.save(account);
    }

    @Override
    public Account updateAccount(Long id, Account account, String user) {
        return repository.findById(id)
                .map(existingAccount -> {
                    existingAccount.setAccountNumber(account.getAccountNumber());
                    existingAccount.setAccountType(account.getAccountType());
                    existingAccount.setInitialBalance(account.getInitialBalance());
                    existingAccount.setStatus(account.getStatus());
                    existingAccount.setPersonId(account.getPersonId());
                    existingAccount.setLastModifiedByUser(user);
                    return repository.save(existingAccount);
                }).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
    }

    @Override
    public void deleteLogic(Long id, String user) {
        Account account = this.findById(id);
        account.setStatus(false);
        account.setLastModifiedByUser(user);
        this.repository.save(account);
    }
}
