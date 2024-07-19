package com.quind.financiera;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.quind.financiera.model.Account;
import com.quind.financiera.model.Movement;
import com.quind.financiera.repository.AccountRepository;
import com.quind.financiera.repository.MovementRepository;
import com.quind.financiera.service.MovementService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovementServiceTest {

	@Autowired
    private MovementService movementService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private MovementRepository movementRepository;
    
	@Test
    public void testCreateMov() {
        Movement movement = new Movement();
        movement.setMovementType(Movement.MovementType.CONSIGNACION.name());
        movement.setAmount(BigDecimal.valueOf(100.0));

        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(200.0));
        account.setAccountType(Account.AccountType_.AHORROS.name());
        
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Mockito.when(movementRepository.save(Mockito.any(Movement.class))).thenReturn(movement);

        Movement newMov = movementService.createMovement(movement, 1L);

        assertNotNull(newMov);
        assertEquals(account, newMov.getAccount());
        assertEquals(LocalDate.now(), newMov.getMovementDate());
        verify(accountRepository, times(1)).findById(1L);
        verify(movementRepository, times(1)).save(movement);
        assertEquals(BigDecimal.valueOf(300.0), account.getBalance());
    }


}
