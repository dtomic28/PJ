
-- Create database
CREATE DATABASE IF NOT EXISTS invoice_system;
USE invoice_system;

-- Company table
CREATE TABLE IF NOT EXISTS company (
                                       id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    tax_number VARCHAR(50) NOT NULL,
    registration_number VARCHAR(50) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    taxpayer BOOLEAN NOT NULL,
    created DATETIME NOT NULL,
    modified DATETIME NOT NULL
    );

-- Item table
CREATE TABLE IF NOT EXISTS item (
                                    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    tax_rate ENUM('STANDARD', 'REDUCED', 'ZERO') NOT NULL,
    discount DECIMAL(5, 2) DEFAULT 0,
    ean VARCHAR(50),
    created DATETIME NOT NULL,
    modified DATETIME NOT NULL
    );

-- Internal Item table
CREATE TABLE IF NOT EXISTS internal_item (
                                             id BINARY(16) PRIMARY KEY,
    item_id BINARY(16) NOT NULL,
    internal_id INT NOT NULL,
    department VARCHAR(100) NOT NULL,
    weight_grams INT DEFAULT 0,
    created DATETIME NOT NULL,
    modified DATETIME NOT NULL,
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
    );

-- Invoice table
CREATE TABLE IF NOT EXISTS invoice (
                                       id BINARY(16) PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL,
    date DATETIME NOT NULL,
    issuer_id BINARY(16) NOT NULL,
    customer_id BINARY(16),
    payment_method VARCHAR(50) DEFAULT 'Cash',
    cashier VARCHAR(255) NOT NULL,
    created DATETIME NOT NULL,
    modified DATETIME NOT NULL,
    FOREIGN KEY (issuer_id) REFERENCES company(id),
    FOREIGN KEY (customer_id) REFERENCES company(id)
    );

-- InvoiceItem table (join table)
CREATE TABLE IF NOT EXISTS invoice_item (
                                            id BINARY(16) PRIMARY KEY,
    invoice_id BINARY(16) NOT NULL,
    item_id BINARY(16) NOT NULL,
    quantity INT DEFAULT 1,
    created DATETIME NOT NULL,
    modified DATETIME NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(id)
    );

INSERT INTO company (id, name, tax_number, registration_number, account_number, email, taxpayer, created, modified) VALUES (X'b6cab4c608e6465698c39137d1ce2742', 'TechCorp', 'SI12345678', '1234567000', 'SI56012345678901234', 'info@techcorp.si', 1, '2025-04-21 11:40:33', '2025-04-21 11:40:33');
INSERT INTO company (id, name, tax_number, registration_number, account_number, email, taxpayer, created, modified) VALUES (X'3030a873058d47ed835940409699ce41', 'GigaStore', 'SI87654321', '8765432100', 'SI56098765432109876', 'contact@gigastore.si', 0, '2025-04-21 11:40:33', '2025-04-21 11:40:33');
INSERT INTO item (id, name, price, tax_rate, discount, ean, created, modified) VALUES (X'438afaa0b2f4475eb1245bf34bf9834b', 'Laptop Pro', 1299.99, 'STANDARD', 5.00, '1234567890123', '2025-04-21 11:40:33', '2025-04-21 11:40:33');
INSERT INTO item (id, name, price, tax_rate, discount, ean, created, modified) VALUES (X'0c32131a20c64351887f4fca2e6cbb74', 'Wireless Mouse', 25.50, 'REDUCED', 0.00, '9876543210987', '2025-04-21 11:40:33', '2025-04-21 11:40:33');
INSERT INTO internal_item (id, item_id, internal_id, department, weight_grams, created, modified) VALUES (X'7547833f959e41fba4ff2932411f3c9b', X'0c32131a20c64351887f4fca2e6cbb74', 101, 'Electronics', 200, '2025-04-21 11:40:33', '2025-04-21 11:40:33');
INSERT INTO invoice (id, invoice_number, date, issuer_id, customer_id, payment_method, cashier, created, modified) VALUES (X'0ab16f9f9a38438898c883b3f84fe3b1', 'INV-001', '2025-04-21 11:40:33', X'b6cab4c608e6465698c39137d1ce2742', X'3030a873058d47ed835940409699ce41', 'Card', 'John Doe', '2025-04-21 11:40:33', '2025-04-21 11:40:33');
INSERT INTO invoice_item (id, invoice_id, item_id, quantity, created, modified) VALUES (X'29c116a38aed4598ab128a4b242e47e9', X'0ab16f9f9a38438898c883b3f84fe3b1', X'438afaa0b2f4475eb1245bf34bf9834b', 1, '2025-04-21 11:40:33', '2025-04-21 11:40:33');
INSERT INTO invoice_item (id, invoice_id, item_id, quantity, created, modified) VALUES (X'd59d67e13c1e4a55af59d1b034f4b731', X'0ab16f9f9a38438898c883b3f84fe3b1', X'0c32131a20c64351887f4fca2e6cbb74', 2, '2025-04-21 11:40:33', '2025-04-21 11:40:33');
