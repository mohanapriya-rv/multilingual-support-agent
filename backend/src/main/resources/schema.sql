-- PostgreSQL Schema for Multilingual Support Agent

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    account_status VARCHAR(30) DEFAULT 'active',
    block_reason TEXT,
    balance NUMERIC(15,2) DEFAULT 0.00,
    preferred_language VARCHAR(20) DEFAULT 'english',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- KYC Records table
CREATE TABLE IF NOT EXISTS kyc_records (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL REFERENCES users(id),
    status VARCHAR(20) DEFAULT 'pending',
    documents_submitted TEXT,
    pending_documents TEXT,
    rejection_reason TEXT,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(20) PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL REFERENCES users(id),
    amount NUMERIC(15,2) NOT NULL,
    merchant VARCHAR(100),
    status VARCHAR(20) DEFAULT 'completed',
    failure_reason TEXT,
    refund_status VARCHAR(20),
    refund_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Escalations table
CREATE TABLE IF NOT EXISTS escalations (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(20),
    original_query TEXT NOT NULL,
    detected_language VARCHAR(20),
    escalation_reason TEXT,
    conversation_history TEXT,
    status VARCHAR(20) DEFAULT 'open',
    assigned_agent VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_kyc_user_id ON kyc_records(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions(status);
CREATE INDEX IF NOT EXISTS idx_escalations_status ON escalations(status);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
