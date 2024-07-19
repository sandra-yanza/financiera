package com.quind.financiera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quind.financiera.model.Account;
import com.quind.financiera.service.AccountService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	 @Autowired
     private AccountService accountService;

     @PostMapping("/{clientId}")
     public ResponseEntity<Account> createAccount(@RequestBody Account account, @PathVariable Long clientId) {
    	 Account newAccount = accountService.createAccount(account, clientId);
         return new ResponseEntity<>(null, HttpStatus.CREATED);
     }
     
     @PutMapping("/{id}")
     public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody Account modifyAccount) {
    	 Account updateAccount = accountService.updateAccount(id, modifyAccount);
         return ResponseEntity.ok(updateAccount);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
    	 accountService.deleteAccount(id);
         return ResponseEntity.noContent().build();
     }

     @GetMapping("/{id}")
     public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
    	 Account account = accountService.getAccountById(id);
         return ResponseEntity.ok(account);
     }


}
