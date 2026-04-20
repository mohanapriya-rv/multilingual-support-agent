-- Demo seed data for H2 database (Fintech & Mutual Funds)

-- Users
INSERT INTO users (id, name, email, phone, account_status, block_reason, balance, preferred_language, created_at) VALUES
('USR001', 'Rajesh Kumar', 'rajesh.kumar@email.com', '9876543210', 'active', NULL, 45230.00, 'hindi', '2023-06-15 10:30:00'),
('USR002', 'Priya Sharma', 'priya.sharma@email.com', '9876543211', 'active', NULL, 12500.50, 'tamil', '2023-08-20 14:45:00'),
('USR003', 'Mohammed Ali', 'mohammed.ali@email.com', '9876543212', 'temporarily_blocked', 'Suspicious large transaction detected. Verification required.', 125000.00, 'kannada', '2023-05-10 09:15:00');

-- Mutual Funds
INSERT INTO mutual_funds (id, name, category, nav, one_year_return, three_year_return, risk_level, min_sip_amount) VALUES
('MF001', 'HDFC Flexi Cap Fund', 'equity', 1523.45, 18.5, 15.2, 'high', 500.00),
('MF002', 'ICICI Prudential Bluechip Fund', 'equity', 78.32, 16.8, 14.5, 'moderate', 500.00),
('MF003', 'SBI Small Cap Fund', 'equity', 142.67, 28.3, 22.1, 'high', 500.00),
('MF004', 'Axis ELSS Tax Saver', 'elss', 89.45, 14.2, 12.8, 'moderate', 500.00),
('MF005', 'HDFC Balanced Advantage', 'hybrid', 425.80, 12.5, 11.2, 'moderate', 500.00),
('MF006', 'ICICI Prudential Liquid Fund', 'debt', 328.90, 6.8, 6.2, 'low', 1000.00),
('MF007', 'Mirae Asset Large Cap', 'equity', 92.15, 19.2, 16.8, 'moderate', 500.00),
('MF008', 'Parag Parikh Flexi Cap', 'equity', 65.78, 21.5, 18.3, 'moderate', 1000.00),
('MF009', 'Nippon India Small Cap', 'equity', 125.40, 32.1, 25.5, 'high', 500.00),
('MF010', 'Kotak Equity Hybrid', 'hybrid', 52.30, 11.8, 10.5, 'moderate', 500.00);

-- User Investments
INSERT INTO user_investments (user_id, fund_id, fund_name, invested_amount, current_value, units, purchase_date) VALUES
('USR001', 'MF001', 'HDFC Flexi Cap Fund', 50000.00, 58500.00, 32.84, '2023-01-15'),
('USR001', 'MF003', 'SBI Small Cap Fund', 30000.00, 38400.00, 210.35, '2023-03-20'),
('USR001', 'MF004', 'Axis ELSS Tax Saver', 150000.00, 171000.00, 1676.91, '2022-12-01'),
('USR002', 'MF002', 'ICICI Prudential Bluechip Fund', 25000.00, 29250.00, 319.18, '2023-06-10'),
('USR002', 'MF005', 'HDFC Balanced Advantage', 40000.00, 45000.00, 93.94, '2023-04-05'),
('USR003', 'MF007', 'Mirae Asset Large Cap', 100000.00, 119200.00, 1085.19, '2022-08-15'),
('USR003', 'MF008', 'Parag Parikh Flexi Cap', 75000.00, 91125.00, 1140.46, '2023-02-28');

-- SIP Records
INSERT INTO sip_records (id, user_id, fund_id, fund_name, amount, frequency, sip_date, status, start_date, next_date, total_installments, completed_installments) VALUES
('SIP001', 'USR001', 'MF001', 'HDFC Flexi Cap Fund', 5000.00, 'monthly', 5, 'active', '2023-01-05', '2024-02-05', 60, 13),
('SIP002', 'USR001', 'MF004', 'Axis ELSS Tax Saver', 12500.00, 'monthly', 1, 'active', '2022-04-01', '2024-02-01', 36, 22),
('SIP003', 'USR002', 'MF002', 'ICICI Prudential Bluechip Fund', 2000.00, 'monthly', 10, 'active', '2023-06-10', '2024-02-10', 24, 8),
('SIP004', 'USR002', 'MF005', 'HDFC Balanced Advantage', 3000.00, 'monthly', 15, 'paused', '2023-04-15', '2024-01-15', 36, 9),
('SIP005', 'USR003', 'MF007', 'Mirae Asset Large Cap', 10000.00, 'monthly', 20, 'active', '2022-08-20', '2024-02-20', 48, 18);

-- KYC Records
INSERT INTO kyc_records (user_id, status, documents_submitted, pending_documents, rejection_reason, last_updated) VALUES
('USR001', 'verified', 'aadhaar,pan,address_proof', NULL, NULL, '2023-07-01 11:00:00'),
('USR002', 'pending', 'aadhaar', 'pan', NULL, '2023-09-10 16:30:00'),
('USR003', 'rejected', 'aadhaar,pan', NULL, 'Aadhaar photo does not match the selfie uploaded. Please resubmit with clear documents.', '2023-09-15 12:00:00');

-- Transactions for USR001 (Rajesh Kumar)
INSERT INTO transactions (id, user_id, amount, status, merchant, failure_reason, created_at, refund_status, refund_date) VALUES
('TXN001', 'USR001', -500.00, 'failed', 'Swiggy', 'Insufficient balance at the time of transaction', '2024-01-15 19:30:00', 'initiated', '2024-01-16'),
('TXN002', 'USR001', 75000.00, 'success', 'Salary Credit - TechCorp', NULL, '2024-01-01 10:00:00', NULL, NULL),
('TXN003', 'USR001', -1200.00, 'success', 'Amazon', NULL, '2024-01-10 15:45:00', NULL, NULL),
('TXN004', 'USR001', -350.00, 'success', 'Zomato', NULL, '2024-01-12 20:15:00', NULL, NULL),
('TXN005', 'USR001', -2500.00, 'success', 'Electricity Bill', NULL, '2024-01-05 09:30:00', NULL, NULL);

-- Transactions for USR002 (Priya Sharma)
INSERT INTO transactions (id, user_id, amount, status, merchant, failure_reason, created_at, refund_status, refund_date) VALUES
('TXN006', 'USR002', 25000.00, 'success', 'UPI Transfer - Self', NULL, '2024-01-08 11:00:00', NULL, NULL),
('TXN007', 'USR002', -800.00, 'pending', 'Flipkart', NULL, '2024-01-14 14:20:00', NULL, NULL);

-- Transactions for USR003 (Mohammed Ali) - Suspicious activity
INSERT INTO transactions (id, user_id, amount, status, merchant, failure_reason, created_at, refund_status, refund_date) VALUES
('TXN008', 'USR003', -95000.00, 'success', 'Unknown Merchant - International', NULL, '2024-01-13 03:45:00', NULL, NULL),
('TXN009', 'USR003', 150000.00, 'success', 'Cash Deposit', NULL, '2024-01-10 16:00:00', NULL, NULL),
('TXN010', 'USR003', -45000.00, 'failed', 'Wire Transfer', 'Transaction blocked due to security concerns', '2024-01-14 02:30:00', NULL, NULL);
