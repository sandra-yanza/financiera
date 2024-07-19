package com.quind.financiera.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quind.financiera.model.Movement;
import com.quind.financiera.service.MovementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/movements")
public class MovementController {

	@Autowired
	private MovementService movementService;

	@PostMapping("/{accountId}")
	public ResponseEntity<Movement> createMovement(@Valid @RequestBody Movement movement, @PathVariable Long accountId) {
		Movement newMovement = movementService.createMovement(movement, accountId);
		return new ResponseEntity<>(newMovement, HttpStatus.CREATED);
	}


	@PostMapping("/transfer")
	public ResponseEntity<Void> createMovBetweenAccounts(@RequestParam BigDecimal amount,
			@RequestParam Long sendAccountId,
			@RequestParam Long receiptAccountId
			) {
		movementService.createMovBetweenAccounts(amount, sendAccountId, receiptAccountId);
		return ResponseEntity.ok().build();
	}


	@GetMapping("/{id}")
    public ResponseEntity<Movement> getMovementById(@PathVariable Long id) {
		Movement movement = movementService.getMovementById(id);
        return ResponseEntity.ok(movement);
    }


}
