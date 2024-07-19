package com.quind.financiera.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quind.financiera.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
