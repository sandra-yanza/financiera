-- Creating database
CREATE DATABASE IF NOT EXISTS financieradb;
USE financieradb;

-- Client Table
CREATE TABLE tbl_Client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_type ENUM('CC', 'CE', 'PP', 'PEP') ,
    identification_number VARCHAR(30) ,
    name VARCHAR(50) ,
    last_name VARCHAR(50) ,
    email VARCHAR(50) ,
    birth_date DATE ,
    creation_date DATE NOT NULL,
    modification_date DATE NOT NULL,
    UNIQUE (identification_number)
);

-- Creting Account Table
CREATE TABLE tbl_Account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_type VARCHAR(10) NOT NULL,
    account_number VARCHAR(10) NOT NULL,
    state VARCHAR(10) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL ,
    gmf_exempt BOOLEAN NOT NULL default 0,
    creation_date DATE NOT NULL,
    modification_date DATE NOT NULL,
    client_id BIGINT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES tbl_Client(id),
    UNIQUE (account_number)
);

-- Creating Movement table
CREATE TABLE tbl_Movement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movement_type VARCHAR(20) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL CHECK (amount >= 0),
    movement_date DATE NOT NULL,
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES tbl_Account(id)
);





