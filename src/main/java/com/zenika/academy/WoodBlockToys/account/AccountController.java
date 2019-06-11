package com.zenika.academy.WoodBlockToys.account;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(path = "/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE)

public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<Account> addAccount(@Valid @RequestBody Account account) {
        try {
            return ResponseEntity.ok(accountService.saveAccount(account));
        } catch (EntityExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseEntity<Account> accountUpdate(@PathVariable Long id, @Valid @RequestBody Account account) {
        try {
            return ResponseEntity.ok(accountService.updateAccount(account, id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>("The account with id " + id + " has been deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(accountService.getAccount(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccountList() {
        return ResponseEntity.ok(accountService.getAccountList());
    }
}
