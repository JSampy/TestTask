package ru.sber.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.test.entity.Account;
import ru.sber.test.entity.AccountMutex;
import ru.sber.test.repository.AccountMutexRepository;
import ru.sber.test.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMutexRepository accountMutexRepository;

    public Optional<Account> getAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Transactional
    public Optional<Account> addAccount(Account account) {
        return Optional.ofNullable(accountRepository.save(account))
                .map(a -> {
                    accountMutexRepository.save(AccountMutex.builder().account(a.getId()).build());
                    return a;
                });
    }
}