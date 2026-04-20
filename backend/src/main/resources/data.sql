-- Demo Users
INSERT INTO users (id, name, email, phone, account_status, block_reason, balance, preferred_language, created_at) VALUES
('USR001', 'Rajesh Kumar', 'rajesh.kumar@email.com', '9876543210', 'active', NULL, 45230.00, 'hindi', '2023-06-15 10:30:00'),
('USR002', 'Priya Sharma', 'priya.sharma@email.com', '9876543211', 'active', NULL, 12500.50, 'tamil', '2023-08-20 14:45:00'),
('USR003', 'Mohammed Ali', 'mohammed.ali@email.com', '9876543212', 'temporarily_blocked', 'Suspicious large transaction detected. Verification required.', 125000.00, 'kannada', '2023-05-10 09:15:00');

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
