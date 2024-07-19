package com.quind.financiera.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quind.financiera.exception.CustomException;
import com.quind.financiera.exception.ResourceNotFoundException;
import com.quind.financiera.model.Account;
import com.quind.financiera.model.Account.AccountType_;
import com.quind.financiera.model.Movement;
import com.quind.financiera.model.Movement.MovementType;
import com.quind.financiera.repository.AccountRepository;
import com.quind.financiera.repository.MovementRepository;

@Service
public class MovementService {
	@Autowired
	private MovementRepository movementRepository;

	@Autowired
	private AccountRepository accountRepository;

	public Movement createMovement(Movement movement, Long accountId) {

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Cuenta no existe - id: " + accountId));

		movement.setAccount(account);
		movement.setMovementDate(LocalDate.now());

		validateFieldsMovement(movement);
		updateBalanceAccount(movement, account);

		return movementRepository.save(movement);
	}

	private void validateFieldsMovement(Movement movement) {

		if (movement.getAmount() == null || movement.getAmount().compareTo(BigDecimal.ZERO)<0) {
			throw new CustomException("La cantidad de la transaccion debe ser superior a 0");
		}

		if (!movement.getMovementType().equals(MovementType.CONSIGNACION.name()) 
				&& !movement.getMovementType().equals(MovementType.RETIRO.name())
				&& !movement.getMovementType().equals(MovementType.TRANSFERENCIA.name())) {
			throw new CustomException("El tipo de movimiento puede ser: CONSIGNACION ó RETIRO ó TRANSFERENCIA" );
		}

	}


	private void updateBalanceAccount(Movement movement, Account account) {

		BigDecimal baln = account.getBalance();

		if (movement.getMovementType().equals(MovementType.CONSIGNACION.name())) {
			baln = baln.add(movement.getAmount());

		} else if (movement.getMovementType().equals(MovementType.RETIRO.name())) {
			if (account.getAccountType().equals(AccountType_.AHORROS.name())
					&& baln.compareTo(movement.getAmount()) < 0) {
				throw new CustomException("El saldo disponible no es suficiente para realizar el retiro");
			}
			baln = baln.subtract(movement.getAmount());
		}
		account.setBalance(baln);
		account.setModificationDate(LocalDate.now());
		accountRepository.save(account);

	}


	public void createMovBetweenAccounts(BigDecimal amount, Long sendAccountId, Long receiptAccountId) {

		Account sendAccount = accountRepository.findById(sendAccountId)
				.orElseThrow(() -> new ResourceNotFoundException("Cuenta que envía no existe - id: " + sendAccountId));

		Account receiptAccount = accountRepository.findById(receiptAccountId)
				.orElseThrow(() -> new ResourceNotFoundException("Cuenta que recibe no existe - id: " + receiptAccountId));

		if (sendAccount.getBalance().compareTo(amount) < 0) {
			throw new CustomException("No es posible la transferencia. El saldo débito es menor");
		}

		Movement debit = new Movement();
		debit.setAccount(sendAccount);
		debit.setMovementType(MovementType.RETIRO.name());
		debit.setAmount(amount);
		debit.setMovementDate(LocalDate.now());
		updateBalanceAccount(debit, sendAccount);
		movementRepository.save(debit);

		Movement credit = new Movement();
		credit.setAccount(receiptAccount);
		credit.setMovementType(MovementType.CONSIGNACION.name());
		credit.setAmount(amount);
		credit.setMovementDate(LocalDate.now());
		updateBalanceAccount(credit, receiptAccount);
		movementRepository.save(credit);
	}


	public Movement getMovementById(Long id) {

		return movementRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Transaccion no existe - id: " + id));

	}

}
