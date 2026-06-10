-- ============================================================
--  PayNova Database Schema
--  MySQL 8.x
-- ============================================================

CREATE DATABASE IF NOT EXISTS paynova_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE paynova_db;

-- ─── Users ────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id                BIGINT          NOT NULL AUTO_INCREMENT,
    name              VARCHAR(100)    NOT NULL,
    email             VARCHAR(255)    NOT NULL,
    phone             VARCHAR(15)     NOT NULL,
    password          VARCHAR(255)    NOT NULL,
    role              ENUM('ADMIN','USER')     NOT NULL DEFAULT 'USER',
    status            ENUM('ACTIVE','BLOCKED','PENDING_VERIFICATION') NOT NULL DEFAULT 'ACTIVE',
    upi_id            VARCHAR(255)    UNIQUE,
    profile_image_url VARCHAR(500),
    created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY idx_email  (email),
    UNIQUE KEY idx_phone  (phone),
    UNIQUE KEY idx_upi_id (upi_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── Wallets ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS wallets (
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    user_id    BIGINT         NOT NULL,
    balance    DECIMAL(15,2)  NOT NULL DEFAULT 0.00,
    is_active  TINYINT(1)              DEFAULT 1,
    version    BIGINT                  DEFAULT 0,
    created_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY idx_wallet_user (user_id),
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── Bank Accounts ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS bank_accounts (
    id                   BIGINT         NOT NULL AUTO_INCREMENT,
    user_id              BIGINT         NOT NULL,
    account_number       VARCHAR(20)    NOT NULL,
    ifsc_code            VARCHAR(11)    NOT NULL,
    bank_name            VARCHAR(100)   NOT NULL,
    account_holder_name  VARCHAR(100)   NOT NULL,
    balance              DECIMAL(15,2)           DEFAULT 0.00,
    is_primary           TINYINT(1)              DEFAULT 0,
    is_verified          TINYINT(1)              DEFAULT 0,
    created_at           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY idx_account_number (account_number),
    KEY idx_bank_user (user_id),
    CONSTRAINT fk_bank_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── Transactions ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS transactions (
    id              BIGINT         NOT NULL AUTO_INCREMENT,
    transaction_id  VARCHAR(50)    NOT NULL,
    sender_id       BIGINT,
    receiver_id     BIGINT,
    amount          DECIMAL(15,2)  NOT NULL,
    type            ENUM('SEND','RECEIVE','ADD_MONEY','REFUND','QR_PAYMENT') NOT NULL,
    status          ENUM('PENDING','SUCCESS','FAILED','REFUNDED') NOT NULL DEFAULT 'PENDING',
    description     VARCHAR(500),
    reference_id    VARCHAR(100),
    failure_reason  VARCHAR(500),
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at    DATETIME,

    PRIMARY KEY (id),
    UNIQUE KEY idx_transaction_id (transaction_id),
    KEY idx_sender   (sender_id),
    KEY idx_receiver (receiver_id),
    KEY idx_created  (created_at),
    CONSTRAINT fk_tx_sender   FOREIGN KEY (sender_id)   REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_tx_receiver FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── QR Payments ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS qr_payments (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    qr_code    VARCHAR(100) NOT NULL,
    user_id    BIGINT       NOT NULL,
    is_active  TINYINT(1)            DEFAULT 1,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY idx_qr_code (qr_code),
    UNIQUE KEY idx_qr_user (user_id),
    CONSTRAINT fk_qr_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── Seed: Default Admin ──────────────────────────────────────────────────────
-- Password: Admin@1234  (BCrypt hash — change in production)
INSERT IGNORE INTO users (name, email, phone, password, role, status, upi_id)
VALUES (
    'Platform Admin',
    'admin@paynova.com',
    '9000000000',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/.aIHdAL0gqGSmacEi',
    'ADMIN',
    'ACTIVE',
    '9000000000@paynova'
);
