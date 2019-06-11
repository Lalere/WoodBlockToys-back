package com.zenika.academy.WoodBlockToys.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(noRollbackFor = {EntityExistsException.class})
    Account saveAccount(Account account) {
        Optional<Account> currentAccountByEmail = accountRepository.findByEmail(account.getEmail());

        if (currentAccountByEmail.isPresent()) {
            throw new EntityExistsException("This account already exists");
        } else {
            return accountRepository.save(account);
        }
    }

    @Transactional(noRollbackFor = {EntityNotFoundException.class})
    Account updateAccount(Account account, Long id) {
        account.setId(id);
        Optional<Account> currentAccount = accountRepository.findById(id);

        if (currentAccount.isEmpty()) {
            throw new EntityNotFoundException("Account not found");
        } else {
            return accountRepository.save(account);
        }
    }

    void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    Account getAccount(Long id) {
        Optional<Account> currentAccount = accountRepository.findById(id);

        if (currentAccount.isEmpty()) {
            throw new EntityNotFoundException("Account not found");
        } else {
            return currentAccount.get();
        }
    }

    List<Account> getAccountList() {
        List<Account> accountList = new ArrayList<>();
        accountRepository.findAll().forEach(accountList::add);
        return accountList;
    }
}
