-- Create the database if it does not exist
CREATE DATABASE IF NOT EXISTS my_database;

-- Switch to the created database
USE my_database;

-- Create Company table
CREATE TABLE IF NOT EXISTS company (
                                       id BINARY(16) PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       tax_number VARCHAR(255) NOT NULL,
                                       registration_number VARCHAR(255) NOT NULL,
                                       account_number VARCHAR(255) NOT NULL,
                                       email VARCHAR(255) NOT NULL,
                                       taxpayer BOOLEAN NOT NULL,
                                       created DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create InternalItem table
CREATE TABLE IF NOT EXISTS internal_item (
                                             id BINARY(16) PRIMARY KEY,
                                             name VARCHAR(255) NOT NULL,
                                             price DECIMAL(10, 2) NOT NULL,
                                             tax_rate ENUM('STANDARD', 'REDUCED', 'ZERO') NOT NULL,
                                             discount DECIMAL(5, 2) DEFAULT 0,
                                             ean VARCHAR(255),
                                             internal_id INT NOT NULL,
                                             department VARCHAR(255) NOT NULL,
                                             weight_grams INT DEFAULT 0,
                                             created DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Item table
CREATE TABLE IF NOT EXISTS item (
                                    id BINARY(16) PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
                                    price DECIMAL(10, 2) NOT NULL,
                                    tax_rate ENUM('STANDARD', 'REDUCED', 'ZERO') NOT NULL,
                                    discount DECIMAL(5, 2) DEFAULT 0,
                                    ean VARCHAR(255),
                                    created DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Invoice table
CREATE TABLE IF NOT EXISTS invoice (
                                       id BINARY(16) PRIMARY KEY,
                                       invoice_number VARCHAR(255) NOT NULL,
                                       date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       issuer_id BINARY(16) NOT NULL,
                                       customer_id BINARY(16),
                                       payment_method VARCHAR(255) DEFAULT 'Cash',
                                       cashier VARCHAR(255) NOT NULL,
                                       created DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (issuer_id) REFERENCES company(id),
                                       FOREIGN KEY (customer_id) REFERENCES company(id)
);

-- Create a table for linking items to invoices (many-to-many relationship)
-- Create a table for linking items to invoices (many-to-many relationship)
-- The table will store either 'item_id' or 'internal_item_id' based on the 'type' column
CREATE TABLE IF NOT EXISTS invoice_items (
                                             invoice_id BINARY(16),
                                             item_id BINARY(16) DEFAULT NULL,
                                             internal_item_id BINARY(16) DEFAULT NULL,
                                             quantity INT DEFAULT 1,
                                             type ENUM('ITEM', 'INTERNAL_ITEM') NOT NULL,  -- Used to distinguish between Item and InternalItem
                                             PRIMARY KEY (invoice_id, item_id, internal_item_id),
                                             FOREIGN KEY (invoice_id) REFERENCES invoice(id),
                                             FOREIGN KEY (item_id) REFERENCES item(id),
                                             FOREIGN KEY (internal_item_id) REFERENCES internal_item(id)
);

