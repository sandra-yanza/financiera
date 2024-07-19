package com.quind.financiera;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.quind.financiera.model.Account;
import com.quind.financiera.model.Account.AccountState;
import com.quind.financiera.model.Account.AccountType_;
import com.quind.financiera.model.Client;
import com.quind.financiera.repository.AccountRepository;
import com.quind.financiera.repository.ClientRepository;
import com.quind.financiera.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
	
	@Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    public void testCreateAccount() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Juan");

        Account account = new Account();
        account.setAccountType(AccountType_.AHORROS.name());
        account.setBalance(BigDecimal.ZERO);
        account.setState(AccountState.ACTIVA.name());

        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        Account newAc = accountService.createAccount(account, 1L);
        assertEquals(AccountType_.AHORROS.name(), newAc.getAccountType());
        assertEquals("53", newAc.getAccountNumber().substring(0, 2));
    }

}
