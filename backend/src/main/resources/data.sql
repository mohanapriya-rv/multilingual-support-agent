-- Demo seed data for H2 database (Fintech & Mutual Funds)

-- Languages (Dynamic - supports any language, just add more rows!)
INSERT INTO languages (code, name, native_name, greeting, fallback_message, escalation_message, out_of_scope_message, flag_emoji, is_active, display_order) VALUES
('en', 'English', 'English', 'Hello', 'Your information: {data}. Please try again or contact our helpline at 1800-XXX-XXXX.', 'Your issue has been escalated to our specialist team. We will contact you within 24 hours. Ticket ID: {ticketId}', 'I can only help with KYC, transactions, mutual funds, and account-related queries. For other questions, please call our helpline at 1800-XXX-XXXX.', '🇬🇧', true, 1),
('hi', 'Hindi', 'हिंदी', 'नमस्ते', 'आपकी जानकारी: {data}। कृपया दोबारा प्रयास करें या हमारी हेल्पलाइन 1800-XXX-XXXX पर कॉल करें।', 'आपका मुद्दा हमारी विशेषज्ञ टीम को भेज दिया गया है। हम 24 घंटे में आपसे संपर्क करेंगे। Ticket ID: {ticketId}', 'मैं केवल KYC, लेनदेन, म्यूचुअल फंड और खाते से संबंधित प्रश्नों में मदद कर सकता हूं। अन्य सवालों के लिए कृपया हमारी हेल्पलाइन 1800-XXX-XXXX पर कॉल करें।', '🇮🇳', true, 2),
('ta', 'Tamil', 'தமிழ்', 'வணக்கம்', 'உங்கள் தகவல்: {data}. மீண்டும் முயற்சிக்கவும் அல்லது எங்கள் உதவி எண் 1800-XXX-XXXX ஐ தொடர்பு கொள்ளவும்.', 'உங்கள் பிரச்சனை எங்கள் நிபுணர் குழுவிற்கு அனுப்பப்பட்டது. 24 மணி நேரத்தில் தொடர்பு கொள்வோம். Ticket ID: {ticketId}', 'KYC, பரிவர்த்தனைகள், மியூச்சுவல் ஃபண்டுகள் மற்றும் கணக்கு தொடர்பான கேள்விகளுக்கு மட்டுமே உதவ முடியும். மற்ற கேள்விகளுக்கு 1800-XXX-XXXX என்ற எண்ணில் தொடர்பு கொள்ளவும்.', '🇮🇳', true, 3),
('te', 'Telugu', 'తెలుగు', 'నమస్కారం', 'మీ సమాచారం: {data}. దయచేసి మళ్ళీ ప్రయత్నించండి లేదా మా హెల్ప్‌లైన్ 1800-XXX-XXXX కు కాల్ చేయండి.', 'మీ సమస్య మా నిపుణుల బృందానికి పంపబడింది. 24 గంటల్లో మీకు తెలియజేస్తాం. Ticket ID: {ticketId}', 'KYC, లావాదేవీలు, మ్యూచువల్ ఫండ్స్ మరియు ఖాతా సంబంధిత ప్రశ్నలకు మాత్రమే సహాయం చేయగలను. ఇతర ప్రశ్నలకు 1800-XXX-XXXX కు కాల్ చేయండి.', '🇮🇳', true, 4),
('kn', 'Kannada', 'ಕನ್ನಡ', 'ನಮಸ್ಕಾರ', 'ನಿಮ್ಮ ಮಾಹಿತಿ: {data}. ದಯವಿಟ್ಟು ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ ಅಥವಾ ನಮ್ಮ ಹೆಲ್ಪ್‌ಲೈನ್ 1800-XXX-XXXX ಗೆ ಕರೆ ಮಾಡಿ.', 'ನಿಮ್ಮ ಸಮಸ್ಯೆಯನ್ನು ನಮ್ಮ ತಜ್ಞ ತಂಡಕ್ಕೆ ಕಳುಹಿಸಲಾಗಿದೆ. 24 ಗಂಟೆಗಳಲ್ಲಿ ಸಂಪರ್ಕಿಸುತ್ತೇವೆ. Ticket ID: {ticketId}', 'KYC, ವಹಿವಾಟುಗಳು, ಮ್ಯೂಚುವಲ್ ಫಂಡ್‌ಗಳು ಮತ್ತು ಖಾತೆ ಸಂಬಂಧಿತ ಪ್ರಶ್ನೆಗಳಿಗೆ ಮಾತ್ರ ಸಹಾಯ ಮಾಡಬಹುದು. ಇತರ ಪ್ರಶ್ನೆಗಳಿಗೆ 1800-XXX-XXXX ಗೆ ಕರೆ ಮಾಡಿ.', '🇮🇳', true, 5),
('ml', 'Malayalam', 'മലയാളം', 'നമസ്കാരം', 'നിങ്ങളുടെ വിവരങ്ങൾ: {data}. വീണ്ടും ശ്രമിക്കുക അല്ലെങ്കിൽ ഞങ്ങളുടെ ഹെൽപ്പ്‌ലൈൻ 1800-XXX-XXXX വിളിക്കുക.', 'നിങ്ങളുടെ പ്രശ്നം ഞങ്ങളുടെ വിദഗ്ധ ടീമിലേക്ക് അയച്ചു. 24 മണിക്കൂറിനുള്ളിൽ ബന്ധപ്പെടും. Ticket ID: {ticketId}', 'KYC, ഇടപാടുകൾ, മ്യൂച്വൽ ഫണ്ടുകൾ, അക്കൗണ്ട് സംബന്ധമായ ചോദ്യങ്ങൾക്ക് മാത്രമേ സഹായിക്കാൻ കഴിയൂ. മറ്റ് ചോദ്യങ്ങൾക്ക് 1800-XXX-XXXX വിളിക്കുക.', '🇮🇳', true, 6),
('bn', 'Bengali', 'বাংলা', 'নমস্কার', 'আপনার তথ্য: {data}. অনুগ্রহ করে আবার চেষ্টা করুন অথবা আমাদের হেল্পলাইন 1800-XXX-XXXX এ কল করুন।', 'আপনার সমস্যা আমাদের বিশেষজ্ঞ দলের কাছে পাঠানো হয়েছে। 24 ঘন্টার মধ্যে যোগাযোগ করা হবে। Ticket ID: {ticketId}', 'KYC, লেনদেন, মিউচুয়াল ফান্ড এবং অ্যাকাউন্ট সম্পর্কিত প্রশ্নে সাহায্য করতে পারি। অন্যান্য প্রশ্নের জন্য 1800-XXX-XXXX এ কল করুন।', '🇮🇳', true, 7),
('mr', 'Marathi', 'मराठी', 'नमस्कार', 'तुमची माहिती: {data}. कृपया पुन्हा प्रयत्न करा किंवा आमच्या हेल्पलाइन 1800-XXX-XXXX वर कॉल करा.', 'तुमचा प्रश्न आमच्या तज्ञ टीमकडे पाठवला आहे. 24 तासांत संपर्क साधला जाईल. Ticket ID: {ticketId}', 'KYC, व्यवहार, म्युच्युअल फंड आणि खात्याशी संबंधित प्रश्नांसाठीच मदत करू शकतो. इतर प्रश्नांसाठी 1800-XXX-XXXX वर कॉल करा.', '🇮🇳', true, 8),
('gu', 'Gujarati', 'ગુજરાતી', 'નમસ્તે', 'તમારી માહિતી: {data}. કૃપા કરીને ફરીથી પ્રયાસ કરો અથવા અમારી હેલ્પલાઈન 1800-XXX-XXXX પર કૉલ કરો.', 'તમારો મુદ્દો અમારી નિષ્ણાત ટીમને મોકલવામાં આવ્યો છે. 24 કલાકમાં સંપર્ક કરીશું. Ticket ID: {ticketId}', 'KYC, વ્યવહારો, મ્યુચ્યુઅલ ફંડ અને ખાતા સંબંધિત પ્રશ્નોમાં જ મદદ કરી શકું છું. અન્ય પ્રશ્નો માટે 1800-XXX-XXXX પર કૉલ કરો.', '🇮🇳', true, 9),
('pa', 'Punjabi', 'ਪੰਜਾਬੀ', 'ਸਤ ਸ੍ਰੀ ਅਕਾਲ', 'ਤੁਹਾਡੀ ਜਾਣਕਾਰੀ: {data}. ਕਿਰਪਾ ਕਰਕੇ ਦੁਬਾਰਾ ਕੋਸ਼ਿਸ਼ ਕਰੋ ਜਾਂ ਸਾਡੀ ਹੈਲਪਲਾਈਨ 1800-XXX-XXXX ਤੇ ਕਾਲ ਕਰੋ।', 'ਤੁਹਾਡਾ ਮੁੱਦਾ ਸਾਡੀ ਮਾਹਰ ਟੀਮ ਨੂੰ ਭੇਜਿਆ ਗਿਆ ਹੈ। 24 ਘੰਟਿਆਂ ਵਿੱਚ ਸੰਪਰਕ ਕਰਾਂਗੇ। Ticket ID: {ticketId}', 'KYC, ਲੈਣ-ਦੇਣ, ਮਿਊਚਲ ਫੰਡ ਅਤੇ ਖਾਤੇ ਨਾਲ ਸਬੰਧਤ ਸਵਾਲਾਂ ਵਿੱਚ ਹੀ ਮਦਦ ਕਰ ਸਕਦਾ ਹਾਂ। ਹੋਰ ਸਵਾਲਾਂ ਲਈ 1800-XXX-XXXX ਤੇ ਕਾਲ ਕਰੋ।', '🇮🇳', true, 10);

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

-- FAQs Knowledge Base
INSERT INTO faqs (category, question, answer, question_hindi, answer_hindi, question_tamil, answer_tamil, keywords, priority) VALUES
('mutual_fund', 'What is NAV?', 'NAV (Net Asset Value) is the per-unit price of a mutual fund calculated by dividing total assets minus liabilities by outstanding units. NAV changes daily based on market performance.', 'NAV क्या है?', 'NAV म्यूचुअल फंड की प्रति-यूनिट कीमत है। इसकी गणना कुल संपत्ति से देनदारियों को घटाकर, यूनिटों से विभाजित करके की जाती है।', NULL, NULL, 'nav,price,unit,value', 10),
('mutual_fund', 'How to start a SIP?', 'To start SIP: 1) Complete KYC 2) Choose fund based on goals 3) Select amount (min ₹500) 4) Choose date (1-28) 5) Setup auto-debit. SIP invests automatically each month.', 'SIP कैसे शुरू करें?', 'SIP शुरू करने के लिए: 1) KYC पूरा करें 2) फंड चुनें 3) राशि चुनें (न्यूनतम ₹500) 4) तारीख चुनें 5) ऑटो-डेबिट सेट करें।', NULL, NULL, 'sip,start,begin,new,monthly', 10),
('mutual_fund', 'How to redeem mutual fund?', 'To redeem: Go to Portfolio > Select fund > Enter units/amount > Confirm. Amount credited in 1-3 days for equity, T+1 for liquid funds. Exit load may apply within 1 year.', 'म्यूचुअल फंड कैसे रिडीम करें?', 'रिडीम करने के लिए: पोर्टफोलियो में जाएं > फंड चुनें > यूनिट/राशि दर्ज करें > पुष्टि करें। राशि 1-3 दिनों में क्रेडिट होगी।', NULL, NULL, 'redeem,withdraw,sell,exit', 9),
('mutual_fund', 'What is exit load?', 'Exit load is a fee (typically 1%) charged when redeeming before a specified period (usually 1 year for equity). Liquid funds usually have no exit load.', 'एग्जिट लोड क्या है?', 'एग्जिट लोड एक शुल्क है जो निर्दिष्ट अवधि से पहले रिडीम करने पर लगता है। इक्विटी फंड के लिए आमतौर पर 1 साल के भीतर 1%।', NULL, NULL, 'exit,load,fee,charge', 8),
('mutual_fund', 'Can I modify SIP amount?', 'Yes! Go to SIP Management > Select SIP > Modify. Changes take effect from next installment. Minimum amount rules still apply.', 'क्या मैं SIP राशि बदल सकता हूं?', 'हां! SIP प्रबंधन में जाएं > SIP चुनें > संशोधित करें। परिवर्तन अगली किस्त से प्रभावी होंगे।', NULL, NULL, 'sip,modify,change,amount', 9),
('mutual_fund', 'How to stop SIP?', 'To pause: SIP Management > Select > Pause (up to 3 months). To stop: Cancel SIP. No charges for stopping. Existing investments remain in portfolio.', 'SIP कैसे रोकें?', 'रोकने के लिए: SIP प्रबंधन > चुनें > रोकें (3 महीने तक)। बंद करने के लिए: SIP रद्द करें। कोई शुल्क नहीं।', NULL, NULL, 'sip,stop,pause,cancel', 9),
('kyc', 'Documents required for KYC?', 'For KYC: 1) PAN Card (mandatory) 2) Aadhaar for address 3) Passport photo 4) Bank details with cancelled cheque. Video KYC takes 5 minutes online.', 'KYC के लिए कौन से दस्तावेज़?', 'KYC के लिए: 1) पैन कार्ड (अनिवार्य) 2) आधार 3) फोटो 4) बैंक विवरण। वीडियो KYC 5 मिनट में।', NULL, NULL, 'kyc,document,pan,aadhaar', 10),
('kyc', 'Why was KYC rejected?', 'Common rejection reasons: 1) Blurry documents 2) Name mismatch in PAN/Aadhaar 3) Photo mismatch 4) Expired documents. Please resubmit clear documents.', 'KYC क्यों रिजेक्ट हुआ?', 'रिजेक्ट के कारण: 1) धुंधले दस्तावेज़ 2) PAN/आधार में नाम मेल नहीं 3) फोटो मेल नहीं 4) एक्सपायर्ड दस्तावेज़।', NULL, NULL, 'kyc,reject,failed,reason', 9),
('account', 'Why is account blocked?', 'Account blocked due to: 1) Suspicious transactions 2) KYC incomplete/expired 3) Multiple failed logins 4) Regulatory issues. Contact support to unblock.', 'अकाउंट ब्लॉक क्यों है?', 'ब्लॉक के कारण: 1) संदिग्ध लेनदेन 2) KYC अधूरा 3) गलत लॉगिन 4) नियामक मुद्दे। अनब्लॉक के लिए सपोर्ट से संपर्क करें।', NULL, NULL, 'account,block,locked,suspend', 10),
('transaction', 'Why did payment fail?', 'Payment fails due to: 1) Insufficient balance 2) Bank server issues 3) Daily limit exceeded 4) Card expired 5) Wrong credentials. Try again or use different method.', 'पेमेंट फेल क्यों हुआ?', 'फेल के कारण: 1) अपर्याप्त बैलेंस 2) बैंक सर्वर समस्या 3) दैनिक सीमा पार 4) कार्ड एक्सपायर्ड 5) गलत जानकारी।', NULL, NULL, 'payment,fail,failed,error', 10);
