package com.quind.financiera.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quind.financiera.exception.CustomException;
import com.quind.financiera.exception.ResourceNotFoundException;
import com.quind.financiera.model.Account;
import com.quind.financiera.model.Account.AccountState;
import com.quind.financiera.model.Account.AccountType_;
import com.quind.financiera.model.Client;
import com.quind.financiera.repository.AccountRepository;
import com.quind.financiera.repository.ClientRepository;

@Service
public class AccountService {
	
	@Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Account createAccount(Account account, Long clientId) {
        Client client = clientRepository.findById(clientId)
        		.orElseThrow(() -> new ResourceNotFoundException("Cliente no existe - id: " + clientId));
        account.setClient(client);

        validateFieldsAccount(account);
            	
        account.setCreationDate(LocalDate.now());
        account.setModificationDate(LocalDate.now());
        account.setAccountNumber(getAccountNumberRandom(account.getAccountType()));
        
        if (account.getAccountType() == AccountType_.AHORROS.toString()) {
        	account.setState(AccountState.ACTIVA.name());
        } 
        
        return accountRepository.save(account);
    }
    
    private String getAccountNumberRandom(String type) {
    	String first = "";
        if(type.equals(AccountType_.AHORROS.toString())) {
        	first = "53";
        }else {
        	first = "33";
        }

        String second = String.format("%08d", new Random().nextInt(100000000));
        return first + second;
    }
    
    private void validateFieldsAccount(Account account) {
    	
    	if (account.getAccountType() == null || account.getAccountType().length() == 0) {
            throw new CustomException("La Cuenta no tiene el dato de Tipo de Cuenta");
        }
        
        if (account.getState() == null ) {
            throw new CustomException("La Cuenta no tiene el dato de Estado");
        }
        
        if (account.getBalance() == null || account.getBalance().compareTo(BigDecimal.ZERO)<0) {
            throw new CustomException("La Cuenta no tiene el saldo ó el saldo no puede ser inferior a 0");
        }
        
        if (!account.getAccountType().equals(AccountType_.AHORROS.name()) 
        		&& !account.getAccountType().equals(AccountType_.CORRIENTE.name())) {
        	throw new CustomException("La cuenta puede ser de tipo: AHORROS ó CORRIENTE" );
        }
        
        if (!account.getState().equals(AccountState.ACTIVA.name()) 
        		&& !account.getState().equals(AccountState.INACTIVA.name())
        		&& !account.getState().equals(AccountState.CANCELADA.name())) {
        	throw new CustomException("La cuenta puede tener el estado: ACTIVA ó INACTIVA ó CANCELADA" );
        }

    	
    }


    public Account updateAccount(Long id, Account modifyAccount) {
    	Account account = accountRepository.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("Cuenta no existe - id: " + id));
    	
    	account.setState(modifyAccount.getState());
    	account.setBalance(modifyAccount.getBalance());
    	account.setModificationDate(LocalDate.now());

    	validateFieldsAccount(account);
    	
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
    	Account account = accountRepository.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("Cuenta no existe - id: " + id));
    	
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new CustomException("No se pudo eliminar. Solo se puede eliminar cuentas con saldo igual a 0");
        }
        accountRepository.delete(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Cuenta no existe - id: " + id));
    }


}
