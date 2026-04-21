-- Seed Data for PostgreSQL - Run manually or via migration tool
-- This file contains sample real-world data

-- Clear existing data (optional - comment out in production)
-- TRUNCATE users, kyc_records, transactions, escalations CASCADE;

-- =============================================
-- USERS (50 sample users across India)
-- =============================================
INSERT INTO users (id, name, email, phone, account_status, block_reason, balance, preferred_language, created_at) VALUES
-- Hindi speaking users (North India)
('USR001', 'Rajesh Kumar', 'rajesh.kumar@email.com', '9876543210', 'active', NULL, 45230.00, 'hindi', '2023-06-15 10:30:00'),
('USR002', 'Priya Sharma', 'priya.sharma@email.com', '9876543211', 'active', NULL, 12500.50, 'hindi', '2023-08-20 14:45:00'),
('USR003', 'Amit Verma', 'amit.verma@email.com', '9876543212', 'active', NULL, 78900.00, 'hindi', '2023-04-10 09:15:00'),
('USR004', 'Neha Gupta', 'neha.gupta@email.com', '9876543213', 'temporarily_blocked', 'Multiple failed login attempts', 34500.00, 'hindi', '2023-07-25 16:20:00'),
('USR005', 'Vikram Singh', 'vikram.singh@email.com', '9876543214', 'active', NULL, 156000.00, 'hindi', '2023-03-05 11:00:00'),
('USR006', 'Anjali Mishra', 'anjali.mishra@email.com', '9876543215', 'active', NULL, 8750.25, 'hindi', '2023-09-12 08:30:00'),
('USR007', 'Rahul Dubey', 'rahul.dubey@email.com', '9876543216', 'blocked', 'Suspicious large transaction detected', 250000.00, 'hindi', '2023-02-18 13:45:00'),
('USR008', 'Sunita Yadav', 'sunita.yadav@email.com', '9876543217', 'active', NULL, 5600.00, 'hindi', '2023-10-01 10:15:00'),
('USR009', 'Deepak Pandey', 'deepak.pandey@email.com', '9876543218', 'active', NULL, 92300.00, 'hindi', '2023-05-22 15:30:00'),
('USR010', 'Kavita Agarwal', 'kavita.agarwal@email.com', '9876543219', 'active', NULL, 18750.00, 'hindi', '2023-08-08 12:00:00'),

-- Tamil speaking users (Tamil Nadu)
('USR011', 'Murugan Rajan', 'murugan.rajan@email.com', '9887654321', 'active', NULL, 67800.00, 'tamil', '2023-06-20 09:00:00'),
('USR012', 'Lakshmi Subramanian', 'lakshmi.s@email.com', '9887654322', 'active', NULL, 23450.00, 'tamil', '2023-07-15 14:30:00'),
('USR013', 'Karthik Narayanan', 'karthik.n@email.com', '9887654323', 'temporarily_blocked', 'KYC documents expired', 45000.00, 'tamil', '2023-04-25 11:45:00'),
('USR014', 'Priya Venkatesh', 'priya.v@email.com', '9887654324', 'active', NULL, 89200.00, 'tamil', '2023-08-30 16:00:00'),
('USR015', 'Senthil Kumar', 'senthil.k@email.com', '9887654325', 'active', NULL, 12000.00, 'tamil', '2023-09-05 10:30:00'),
('USR016', 'Meena Krishnan', 'meena.k@email.com', '9887654326', 'active', NULL, 56700.00, 'tamil', '2023-05-10 13:15:00'),
('USR017', 'Anand Ramasamy', 'anand.r@email.com', '9887654327', 'active', NULL, 34500.00, 'tamil', '2023-06-28 08:45:00'),
('USR018', 'Divya Palani', 'divya.p@email.com', '9887654328', 'blocked', 'Fraudulent activity reported', 0.00, 'tamil', '2023-03-15 17:00:00'),
('USR019', 'Ravi Chandran', 'ravi.c@email.com', '9887654329', 'active', NULL, 78900.00, 'tamil', '2023-07-22 12:30:00'),
('USR020', 'Saranya Mohan', 'saranya.m@email.com', '9887654330', 'active', NULL, 41200.00, 'tamil', '2023-08-18 09:45:00'),

-- Telugu speaking users (Andhra Pradesh & Telangana)
('USR021', 'Venkata Rao', 'venkata.rao@email.com', '9898765432', 'active', NULL, 125000.00, 'telugu', '2023-05-05 10:00:00'),
('USR022', 'Padma Reddy', 'padma.reddy@email.com', '9898765433', 'active', NULL, 67500.00, 'telugu', '2023-06-12 14:15:00'),
('USR023', 'Srinivas Murthy', 'srinivas.m@email.com', '9898765434', 'active', NULL, 23400.00, 'telugu', '2023-07-20 11:30:00'),
('USR024', 'Lakshmi Devi', 'lakshmi.d@email.com', '9898765435', 'temporarily_blocked', 'Address verification pending', 89000.00, 'telugu', '2023-04-15 16:45:00'),
('USR025', 'Ramesh Babu', 'ramesh.b@email.com', '9898765436', 'active', NULL, 45600.00, 'telugu', '2023-08-25 09:00:00'),
('USR026', 'Swathi Naidu', 'swathi.n@email.com', '9898765437', 'active', NULL, 12300.00, 'telugu', '2023-09-10 13:30:00'),
('USR027', 'Krishna Prasad', 'krishna.p@email.com', '9898765438', 'active', NULL, 78000.00, 'telugu', '2023-06-08 15:00:00'),
('USR028', 'Anitha Kumari', 'anitha.k@email.com', '9898765439', 'active', NULL, 34200.00, 'telugu', '2023-07-30 10:45:00'),
('USR029', 'Suresh Varma', 'suresh.v@email.com', '9898765440', 'blocked', 'Multiple chargebacks', 0.00, 'telugu', '2023-02-28 12:15:00'),
('USR030', 'Bhavani Shankar', 'bhavani.s@email.com', '9898765441', 'active', NULL, 56700.00, 'telugu', '2023-08-05 08:30:00'),

-- Kannada speaking users (Karnataka)
('USR031', 'Manjunath Gowda', 'manjunath.g@email.com', '9909876543', 'active', NULL, 98000.00, 'kannada', '2023-04-20 09:30:00'),
('USR032', 'Shwetha Rao', 'shwetha.r@email.com', '9909876544', 'active', NULL, 45600.00, 'kannada', '2023-05-28 14:00:00'),
('USR033', 'Naveen Kumar', 'naveen.k@email.com', '9909876545', 'temporarily_blocked', 'Suspicious login from new device', 67800.00, 'kannada', '2023-06-15 11:15:00'),
('USR034', 'Rekha Shetty', 'rekha.s@email.com', '9909876546', 'active', NULL, 23400.00, 'kannada', '2023-07-10 16:30:00'),
('USR035', 'Prakash Hegde', 'prakash.h@email.com', '9909876547', 'active', NULL, 89100.00, 'kannada', '2023-08-22 10:00:00'),
('USR036', 'Deepa Murthy', 'deepa.m@email.com', '9909876548', 'active', NULL, 12500.00, 'kannada', '2023-09-18 13:45:00'),
('USR037', 'Sunil Patil', 'sunil.p@email.com', '9909876549', 'active', NULL, 56000.00, 'kannada', '2023-06-25 08:15:00'),
('USR038', 'Asha Bhat', 'asha.b@email.com', '9909876550', 'active', NULL, 34500.00, 'kannada', '2023-07-28 15:30:00'),
('USR039', 'Raghu Nayak', 'raghu.n@email.com', '9909876551', 'blocked', 'Identity theft suspected', 0.00, 'kannada', '2023-03-10 12:00:00'),
('USR040', 'Vidya Kulkarni', 'vidya.k@email.com', '9909876552', 'active', NULL, 78200.00, 'kannada', '2023-08-12 09:45:00'),

-- English speaking users (Metro cities)
('USR041', 'John Mathew', 'john.mathew@email.com', '9912345678', 'active', NULL, 145000.00, 'english', '2023-03-25 10:30:00'),
('USR042', 'Sarah Thomas', 'sarah.thomas@email.com', '9912345679', 'active', NULL, 67800.00, 'english', '2023-04-30 14:45:00'),
('USR043', 'Michael D''Souza', 'michael.d@email.com', '9912345680', 'active', NULL, 34500.00, 'english', '2023-05-18 11:00:00'),
('USR044', 'Jennifer Fernandes', 'jennifer.f@email.com', '9912345681', 'temporarily_blocked', 'Payment verification required', 89200.00, 'english', '2023-06-22 16:15:00'),
('USR045', 'David Wilson', 'david.wilson@email.com', '9912345682', 'active', NULL, 23400.00, 'english', '2023-07-15 09:30:00'),
('USR046', 'Emily Johnson', 'emily.j@email.com', '9912345683', 'active', NULL, 56700.00, 'english', '2023-08-08 13:00:00'),
('USR047', 'Robert Brown', 'robert.b@email.com', '9912345684', 'active', NULL, 12000.00, 'english', '2023-09-02 15:45:00'),
('USR048', 'Amanda Lee', 'amanda.lee@email.com', '9912345685', 'active', NULL, 78900.00, 'english', '2023-06-30 10:15:00'),
('USR049', 'Christopher Martin', 'chris.m@email.com', '9912345686', 'blocked', 'Account compromised', 0.00, 'english', '2023-02-14 12:30:00'),
('USR050', 'Jessica Davis', 'jessica.d@email.com', '9912345687', 'active', NULL, 41200.00, 'english', '2023-08-20 08:00:00')
ON CONFLICT (id) DO NOTHING;

-- =============================================
-- KYC RECORDS
-- =============================================
INSERT INTO kyc_records (user_id, status, documents_submitted, pending_documents, rejection_reason, last_updated) VALUES
('USR001', 'verified', 'aadhaar,pan,address_proof', NULL, NULL, '2023-07-01 11:00:00'),
('USR002', 'verified', 'aadhaar,pan,voter_id', NULL, NULL, '2023-09-01 10:30:00'),
('USR003', 'verified', 'aadhaar,pan,passport', NULL, NULL, '2023-05-15 14:00:00'),
('USR004', 'pending', 'aadhaar,pan', 'address_proof', NULL, '2023-08-10 09:00:00'),
('USR005', 'verified', 'aadhaar,pan,driving_license', NULL, NULL, '2023-04-20 16:30:00'),
('USR006', 'pending', 'aadhaar', 'pan,address_proof', NULL, '2023-09-25 11:15:00'),
('USR007', 'rejected', 'aadhaar,pan', NULL, 'PAN card image unclear, please resubmit', '2023-03-01 13:45:00'),
('USR008', 'verified', 'aadhaar,pan,voter_id', NULL, NULL, '2023-10-10 10:00:00'),
('USR009', 'verified', 'aadhaar,pan,address_proof', NULL, NULL, '2023-06-05 15:30:00'),
('USR010', 'pending', 'aadhaar,pan', 'selfie_verification', NULL, '2023-08-20 12:00:00'),
('USR011', 'verified', 'aadhaar,pan,address_proof', NULL, NULL, '2023-07-10 09:30:00'),
('USR012', 'verified', 'aadhaar,pan,driving_license', NULL, NULL, '2023-08-05 14:15:00'),
('USR013', 'expired', 'aadhaar,pan', 'updated_address_proof', 'Address proof older than 3 months', '2023-05-10 11:00:00'),
('USR014', 'verified', 'aadhaar,pan,passport', NULL, NULL, '2023-09-15 16:45:00'),
('USR015', 'pending', 'aadhaar', 'pan,address_proof', NULL, '2023-09-20 10:30:00'),
('USR021', 'verified', 'aadhaar,pan,voter_id', NULL, NULL, '2023-06-01 09:00:00'),
('USR022', 'verified', 'aadhaar,pan,address_proof', NULL, NULL, '2023-07-05 14:30:00'),
('USR024', 'pending', 'aadhaar,pan', 'address_proof', 'Address verification pending', '2023-05-01 11:45:00'),
('USR031', 'verified', 'aadhaar,pan,driving_license', NULL, NULL, '2023-05-15 09:15:00'),
('USR032', 'verified', 'aadhaar,pan,passport', NULL, NULL, '2023-06-20 14:00:00'),
('USR033', 'pending', 'aadhaar,pan', 'video_kyc', NULL, '2023-07-01 10:45:00'),
('USR041', 'verified', 'passport,pan,address_proof', NULL, NULL, '2023-04-15 13:30:00'),
('USR042', 'verified', 'aadhaar,pan,voter_id', NULL, NULL, '2023-05-25 16:00:00'),
('USR044', 'pending', 'aadhaar,pan', 'bank_statement', NULL, '2023-07-10 11:30:00');

-- =============================================
-- TRANSACTIONS (Sample transactions for various scenarios)
-- =============================================
INSERT INTO transactions (id, user_id, amount, merchant, status, failure_reason, refund_status, refund_date, created_at) VALUES
-- Successful transactions
('TXN001', 'USR001', 2500.00, 'Amazon India', 'completed', NULL, NULL, NULL, '2024-01-15 10:30:00'),
('TXN002', 'USR001', 1500.00, 'Swiggy', 'completed', NULL, NULL, NULL, '2024-01-18 19:45:00'),
('TXN003', 'USR001', 5000.00, 'Flipkart', 'completed', NULL, NULL, NULL, '2024-01-20 14:00:00'),
('TXN004', 'USR002', 3200.00, 'BigBasket', 'completed', NULL, NULL, NULL, '2024-01-16 11:15:00'),
('TXN005', 'USR002', 850.00, 'Zomato', 'completed', NULL, NULL, NULL, '2024-01-19 20:30:00'),
('TXN006', 'USR003', 15000.00, 'MakeMyTrip', 'completed', NULL, NULL, NULL, '2024-01-12 09:00:00'),
('TXN007', 'USR003', 2800.00, 'Myntra', 'completed', NULL, NULL, NULL, '2024-01-17 16:45:00'),

-- Failed transactions
('TXN008', 'USR001', 8000.00, 'Croma', 'failed', 'Insufficient balance', NULL, NULL, '2024-01-22 13:30:00'),
('TXN009', 'USR004', 4500.00, 'Amazon India', 'failed', 'Card declined by issuer', NULL, NULL, '2024-01-21 15:00:00'),
('TXN010', 'USR011', 6000.00, 'Flipkart', 'failed', 'Transaction timeout', NULL, NULL, '2024-01-20 18:15:00'),
('TXN011', 'USR021', 3500.00, 'Swiggy', 'failed', 'OTP verification failed', NULL, NULL, '2024-01-19 21:00:00'),
('TXN012', 'USR031', 12000.00, 'BookMyShow', 'failed', 'Daily limit exceeded', NULL, NULL, '2024-01-18 17:30:00'),

-- Refunded transactions
('TXN013', 'USR002', 4200.00, 'Ajio', 'completed', NULL, 'refunded', '2024-01-25', '2024-01-10 12:00:00'),
('TXN014', 'USR005', 7500.00, 'Nykaa', 'completed', NULL, 'refunded', '2024-01-23', '2024-01-08 14:30:00'),
('TXN015', 'USR012', 2100.00, 'Zomato', 'completed', NULL, 'refund_pending', NULL, '2024-01-15 19:00:00'),
('TXN016', 'USR022', 5600.00, 'Amazon India', 'completed', NULL, 'refund_initiated', NULL, '2024-01-12 11:45:00'),
('TXN017', 'USR032', 3800.00, 'Flipkart', 'completed', NULL, 'refunded', '2024-01-20', '2024-01-05 10:15:00'),

-- Recent transactions for testing
('TXN018', 'USR001', 1200.00, 'Uber', 'completed', NULL, NULL, NULL, '2024-01-25 08:00:00'),
('TXN019', 'USR001', 450.00, 'Starbucks', 'completed', NULL, NULL, NULL, '2024-01-25 09:30:00'),
('TXN020', 'USR001', 2800.00, 'Decathlon', 'completed', NULL, NULL, NULL, '2024-01-24 17:00:00'),
('TXN021', 'USR011', 1800.00, 'Dominos', 'completed', NULL, NULL, NULL, '2024-01-25 20:15:00'),
('TXN022', 'USR011', 5500.00, 'Reliance Digital', 'completed', NULL, NULL, NULL, '2024-01-23 15:30:00'),
('TXN023', 'USR021', 950.00, 'KFC', 'completed', NULL, NULL, NULL, '2024-01-25 13:00:00'),
('TXN024', 'USR021', 3200.00, 'Lifestyle', 'completed', NULL, NULL, NULL, '2024-01-22 16:45:00'),
('TXN025', 'USR031', 2400.00, 'PVR Cinemas', 'completed', NULL, NULL, NULL, '2024-01-24 19:30:00'),
('TXN026', 'USR031', 6800.00, 'Tanishq', 'completed', NULL, NULL, NULL, '2024-01-21 11:00:00'),
('TXN027', 'USR041', 4500.00, 'Apple Store', 'completed', NULL, NULL, NULL, '2024-01-25 14:15:00'),
('TXN028', 'USR041', 1100.00, 'Netflix', 'completed', NULL, NULL, NULL, '2024-01-20 10:00:00'),
('TXN029', 'USR041', 8900.00, 'Samsung', 'completed', NULL, NULL, NULL, '2024-01-18 12:30:00'),

-- High value transactions
('TXN030', 'USR005', 75000.00, 'HDFC Bank EMI', 'completed', NULL, NULL, NULL, '2024-01-15 09:00:00'),
('TXN031', 'USR007', 150000.00, 'Unknown Merchant', 'failed', 'Flagged for review - unusual amount', NULL, NULL, '2024-01-10 23:45:00'),
('TXN032', 'USR014', 45000.00, 'IRCTC', 'completed', NULL, NULL, NULL, '2024-01-12 08:30:00'),
('TXN033', 'USR027', 62000.00, 'LIC Premium', 'completed', NULL, NULL, NULL, '2024-01-05 10:00:00'),
('TXN034', 'USR035', 38000.00, 'Croma', 'completed', NULL, NULL, NULL, '2024-01-08 15:45:00'),
('TXN035', 'USR048', 55000.00, 'Dell India', 'completed', NULL, NULL, NULL, '2024-01-03 11:30:00')
ON CONFLICT (id) DO NOTHING;

-- =============================================
-- SAMPLE ESCALATIONS
-- =============================================
INSERT INTO escalations (user_id, original_query, detected_language, escalation_reason, conversation_history, status, assigned_agent, created_at) VALUES
('USR007', 'मेरे अकाउंट से बिना मेरी जानकारी के पैसे कट गए', 'hindi', 'Fraud complaint - unauthorized transaction', '[{"role":"user","content":"मेरे अकाउंट से बिना मेरी जानकारी के पैसे कट गए"}]', 'open', 'Agent_Priya', '2024-01-22 10:30:00'),
('USR018', 'என் கணக்கை யாரோ hack செய்துவிட்டார்கள்', 'tamil', 'Security breach reported - account compromised', '[{"role":"user","content":"என் கணக்கை யாரோ hack செய்துவிட்டார்கள்"}]', 'in_progress', 'Agent_Kumar', '2024-01-21 14:15:00'),
('USR029', 'నా రీఫండ్ రాలేదు, 3 వారాలు అయింది', 'telugu', 'Refund delayed beyond SLA', '[{"role":"user","content":"నా రీఫండ్ రాలేదు, 3 వారాలు అయింది"}]', 'resolved', 'Agent_Srinivas', '2024-01-15 11:00:00'),
('USR039', 'ನನ್ನ ಖಾತೆ block ಆಗಿದೆ, ಯಾಕೆ?', 'kannada', 'Account blocked - customer dispute', '[{"role":"user","content":"ನನ್ನ ಖಾತೆ block ಆಗಿದೆ, ಯಾಕೆ?"}]', 'open', 'Agent_Deepak', '2024-01-23 16:45:00'),
('USR049', 'Someone accessed my account from another country', 'english', 'Suspicious login detected - international access', '[{"role":"user","content":"Someone accessed my account from another country"}]', 'in_progress', 'Agent_John', '2024-01-20 09:30:00');

-- =============================================
-- ANALYTICS EVENTS (Sample data for dashboard)
-- Uses NOW() with offsets so data is always recent
-- =============================================
INSERT INTO analytics_events (user_id, session_id, language, input_type, intent_category, intent_type, response_given, escalated, confidence, timestamp) VALUES
-- Today's events
('USR001', 'sess_today_001', 'hindi', 'text', 'kyc', 'kyc_status', true, false, 0.95, NOW() - INTERVAL '1 hour'),
('USR002', 'sess_today_002', 'hindi', 'text', 'transaction', 'transaction_status', true, false, 0.88, NOW() - INTERVAL '2 hours'),
('USR011', 'sess_today_003', 'tamil', 'text', 'mutual_fund', 'fund_info', true, false, 0.92, NOW() - INTERVAL '3 hours'),
('USR021', 'sess_today_004', 'telugu', 'text', 'kyc', 'kyc_documents', true, false, 0.90, NOW() - INTERVAL '4 hours'),
('USR031', 'sess_today_005', 'kannada', 'text', 'account', 'account_status', true, false, 0.85, NOW() - INTERVAL '5 hours'),
('USR041', 'sess_today_006', 'english', 'text', 'transaction', 'payment_failed', true, false, 0.91, NOW() - INTERVAL '6 hours'),
('USR003', 'sess_today_007', 'hindi', 'text', 'mutual_fund', 'sip_info', true, false, 0.87, NOW() - INTERVAL '7 hours'),
('USR012', 'sess_today_008', 'tamil', 'text', 'kyc', 'kyc_status', true, true, 0.45, NOW() - INTERVAL '8 hours'),
('USR005', 'sess_today_009', 'hindi', 'voice', 'transaction', 'refund_status', true, false, 0.93, NOW() - INTERVAL '9 hours'),
('USR022', 'sess_today_010', 'telugu', 'text', 'account', 'account_blocked', true, true, 0.40, NOW() - INTERVAL '10 hours'),

-- Yesterday's events
('USR001', 'sess_yest_001', 'hindi', 'text', 'mutual_fund', 'portfolio_view', true, false, 0.96, NOW() - INTERVAL '1 day' - INTERVAL '2 hours'),
('USR014', 'sess_yest_002', 'tamil', 'text', 'kyc', 'kyc_status', true, false, 0.89, NOW() - INTERVAL '1 day' - INTERVAL '3 hours'),
('USR025', 'sess_yest_003', 'telugu', 'text', 'transaction', 'transaction_history', true, false, 0.91, NOW() - INTERVAL '1 day' - INTERVAL '4 hours'),
('USR032', 'sess_yest_004', 'kannada', 'voice', 'mutual_fund', 'sip_info', true, false, 0.84, NOW() - INTERVAL '1 day' - INTERVAL '5 hours'),
('USR042', 'sess_yest_005', 'english', 'text', 'account', 'update_details', true, false, 0.88, NOW() - INTERVAL '1 day' - INTERVAL '6 hours'),
('USR006', 'sess_yest_006', 'hindi', 'text', 'kyc', 'kyc_documents', true, false, 0.92, NOW() - INTERVAL '1 day' - INTERVAL '7 hours'),
('USR015', 'sess_yest_007', 'tamil', 'text', 'transaction', 'payment_failed', true, true, 0.38, NOW() - INTERVAL '1 day' - INTERVAL '8 hours'),
('USR035', 'sess_yest_008', 'kannada', 'text', 'mutual_fund', 'fund_info', true, false, 0.90, NOW() - INTERVAL '1 day' - INTERVAL '9 hours'),

-- 2 days ago
('USR001', 'sess_2d_001', 'hindi', 'text', 'transaction', 'refund_status', true, false, 0.94, NOW() - INTERVAL '2 days' - INTERVAL '1 hour'),
('USR011', 'sess_2d_002', 'tamil', 'voice', 'kyc', 'kyc_status', true, false, 0.87, NOW() - INTERVAL '2 days' - INTERVAL '3 hours'),
('USR021', 'sess_2d_003', 'telugu', 'text', 'mutual_fund', 'portfolio_view', true, false, 0.91, NOW() - INTERVAL '2 days' - INTERVAL '5 hours'),
('USR041', 'sess_2d_004', 'english', 'text', 'account', 'account_status', true, false, 0.86, NOW() - INTERVAL '2 days' - INTERVAL '7 hours'),
('USR007', 'sess_2d_005', 'hindi', 'text', 'account', 'account_blocked', true, true, 0.35, NOW() - INTERVAL '2 days' - INTERVAL '9 hours'),
('USR031', 'sess_2d_006', 'kannada', 'text', 'transaction', 'transaction_status', true, false, 0.89, NOW() - INTERVAL '2 days' - INTERVAL '11 hours'),

-- 3 days ago
('USR002', 'sess_3d_001', 'hindi', 'text', 'mutual_fund', 'sip_info', true, false, 0.93, NOW() - INTERVAL '3 days' - INTERVAL '2 hours'),
('USR012', 'sess_3d_002', 'tamil', 'text', 'transaction', 'payment_failed', true, false, 0.85, NOW() - INTERVAL '3 days' - INTERVAL '4 hours'),
('USR022', 'sess_3d_003', 'telugu', 'text', 'kyc', 'kyc_documents', true, false, 0.90, NOW() - INTERVAL '3 days' - INTERVAL '6 hours'),
('USR042', 'sess_3d_004', 'english', 'voice', 'mutual_fund', 'fund_info', true, false, 0.88, NOW() - INTERVAL '3 days' - INTERVAL '8 hours'),
('USR032', 'sess_3d_005', 'kannada', 'text', 'account', 'update_details', true, false, 0.86, NOW() - INTERVAL '3 days' - INTERVAL '10 hours'),

-- 4-7 days ago (spread across the week)
('USR005', 'sess_4d_001', 'hindi', 'text', 'kyc', 'kyc_status', true, false, 0.95, NOW() - INTERVAL '4 days' - INTERVAL '3 hours'),
('USR014', 'sess_4d_002', 'tamil', 'text', 'mutual_fund', 'portfolio_view', true, false, 0.91, NOW() - INTERVAL '4 days' - INTERVAL '6 hours'),
('USR025', 'sess_5d_001', 'telugu', 'text', 'transaction', 'refund_status', true, false, 0.87, NOW() - INTERVAL '5 days' - INTERVAL '2 hours'),
('USR035', 'sess_5d_002', 'kannada', 'text', 'kyc', 'kyc_status', true, false, 0.92, NOW() - INTERVAL '5 days' - INTERVAL '8 hours'),
('USR045', 'sess_6d_001', 'english', 'text', 'account', 'account_status', true, false, 0.89, NOW() - INTERVAL '6 days' - INTERVAL '4 hours'),
('USR008', 'sess_6d_002', 'hindi', 'voice', 'transaction', 'transaction_history', true, true, 0.42, NOW() - INTERVAL '6 days' - INTERVAL '10 hours'),
('USR019', 'sess_7d_001', 'tamil', 'text', 'mutual_fund', 'sip_info', true, false, 0.88, NOW() - INTERVAL '7 days' - INTERVAL '5 hours'),
('USR027', 'sess_7d_002', 'telugu', 'text', 'kyc', 'kyc_documents', true, false, 0.90, NOW() - INTERVAL '7 days' - INTERVAL '9 hours');
