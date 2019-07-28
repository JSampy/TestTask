package ru.sber.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.test.entity.Account;
import ru.sber.test.service.AccountService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Account addAccount(
            @RequestBody Account account
    ) {
        return accountService.addAccount(account)
                .orElseThrow(IllegalArgumentException::new);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Account getAccount(
            @PathVariable(name = "id") Long id
    ) {
        return accountService.getAccount(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
