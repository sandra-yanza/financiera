package com.quind.financiera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quind.financiera.model.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
