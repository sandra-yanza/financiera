package com.quind.financiera.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quind.financiera.model.Movement;

public interface MovementRepository extends JpaRepository<Movement, Long> {
}



