-- Creating database
CREATE DATABASE IF NOT EXISTS financieradb;
USE financieradb;

-- Client Table
-- select * from tbl_Client
CREATE TABLE tbl_Client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_type ENUM('CC', 'CE', 'PP', 'PEP') NOT NULL,
    identification_number VARCHAR(30) NOT NULL,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    creation_date DATE NOT NULL,
    modification_date DATE NOT NULL,
    UNIQUE (identification_number)
);

-- Creting Account Table
CREATE TABLE tbl_Account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_type ENUM('AHORROS', 'CORRIENTE') NOT NULL,
    account_number VARCHAR(10) NOT NULL,
    state ENUM('ACTIVA', 'INACTIVA', 'CANCELADA') NOT NULL,
    balance DECIMAL(15, 2) NOT NULL CHECK (balance >= 0),
    gmf_exempt BOOLEAN NOT NULL default 0,
    creation_date DATE NOT NULL,
    modification_date DATE NOT NULL,
    client_id BIGINT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES tbl_Client(id),
    UNIQUE (account_number)
);

-- select * from tbl_Account;

-- Creating Movement table
CREATE TABLE tbl_Movement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movement_type ENUM('CONSIGNACION', 'RETIRO', 'TRANSFERENCIA') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL CHECK (amount >= 0),
    movement_date DATE NOT NULL,
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES tbl_Account(id)
);



