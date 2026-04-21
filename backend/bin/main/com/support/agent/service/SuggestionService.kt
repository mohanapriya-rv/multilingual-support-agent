package com.support.agent.service

import org.springframework.stereotype.Service

@Service
class SuggestionService {

    private val suggestions = mapOf(
        "kyc" to mapOf(
            "english" to listOf("Check KYC status", "Upload documents", "KYC pending?", "Update PAN details", "Link Aadhaar"),
            "hindi" to listOf("KYC स्थिति जांचें", "दस्तावेज़ अपलोड करें", "KYC पेंडिंग क्यों?", "PAN अपडेट करें", "आधार लिंक करें"),
            "tamil" to listOf("KYC நிலை சரிபார்க்க", "ஆவணங்கள் பதிவேற்றம்", "KYC pending ஏன்?", "PAN புதுப்பிக்க", "ஆதார் இணைக்க"),
            "telugu" to listOf("KYC స్థితి చూడండి", "డాక్యుమెంట్స్ అప్‌లోడ్", "KYC pending ఎందుకు?", "PAN అప్‌డేట్", "ఆధార్ లింక్"),
            "kannada" to listOf("KYC ಸ್ಥಿತಿ ಪರಿಶೀಲಿಸಿ", "ಡಾಕ್ಯುಮೆಂಟ್ ಅಪ್‌ಲೋಡ್", "KYC pending ಏಕೆ?", "PAN ಅಪ್‌ಡೇಟ್", "ಆಧಾರ್ ಲಿಂಕ್"),
            "malayalam" to listOf("KYC സ്റ്റാറ്റസ് പരിശോധിക്കുക", "ഡോക്യുമെന്റ് അപ്‌ലോഡ്", "KYC pending എന്തുകൊണ്ട്?", "PAN അപ്‌ഡേറ്റ്", "ആധാർ ലിങ്ക്"),
            "bengali" to listOf("KYC স্ট্যাটাস দেখুন", "ডকুমেন্ট আপলোড", "KYC pending কেন?", "PAN আপডেট", "আধার লিঙ্ক"),
            "marathi" to listOf("KYC स्थिती तपासा", "कागदपत्रे अपलोड", "KYC pending का?", "PAN अपडेट", "आधार लिंक"),
            "gujarati" to listOf("KYC સ્થિતિ તપાસો", "ડોક્યુમેન્ટ અપલોડ", "KYC pending કેમ?", "PAN અપડેટ", "આધાર લિંક"),
            "punjabi" to listOf("KYC ਸਥਿਤੀ ਚੈੱਕ ਕਰੋ", "ਦਸਤਾਵੇਜ਼ ਅੱਪਲੋਡ", "KYC pending ਕਿਉਂ?", "PAN ਅੱਪਡੇਟ", "ਆਧਾਰ ਲਿੰਕ")
        ),
        "mutual_fund" to mapOf(
            "english" to listOf("View portfolio", "Check SIP status", "Start new SIP", "Redeem funds", "Fund returns"),
            "hindi" to listOf("पोर्टफोलियो देखें", "SIP स्थिति जांचें", "नया SIP शुरू करें", "फंड रिडीम करें", "रिटर्न देखें"),
            "tamil" to listOf("போர்ட்ஃபோலியோ பார்க்க", "SIP நிலை சரிபார்க்க", "புதிய SIP தொடங்க", "ஃபண்ட் ரிடீம்", "ரிட்டர்ன்ஸ் பார்க்க"),
            "telugu" to listOf("పోర్ట్‌ఫోలియో చూడండి", "SIP స్థితి చూడండి", "కొత్త SIP ప్రారంభం", "ఫండ్ రిడీమ్", "రిటర్న్స్ చూడండి"),
            "kannada" to listOf("ಪೋರ್ಟ್‌ಫೋಲಿಯೋ ನೋಡಿ", "SIP ಸ್ಥಿತಿ ಪರಿಶೀಲಿಸಿ", "ಹೊಸ SIP ಪ್ರಾರಂಭ", "ಫಂಡ್ ರಿಡೀಮ್", "ರಿಟರ್ನ್ಸ್ ನೋಡಿ"),
            "malayalam" to listOf("പോർട്ട്‌ഫോളിയോ കാണുക", "SIP സ്റ്റാറ്റസ്", "പുതിയ SIP ആരംഭിക്കുക", "ഫണ്ട് റിഡീം", "റിട്ടേൺസ് കാണുക"),
            "bengali" to listOf("পোর্টফোলিও দেখুন", "SIP স্ট্যাটাস", "নতুন SIP শুরু", "ফান্ড রিডিম", "রিটার্নস দেখুন"),
            "marathi" to listOf("पोर्टफोलिओ पहा", "SIP स्थिती", "नवीन SIP सुरू करा", "फंड रिडीम", "रिटर्न्स पहा"),
            "gujarati" to listOf("પોર્ટફોલિયો જુઓ", "SIP સ્થિતિ", "નવી SIP શરૂ કરો", "ફંડ રિડીમ", "રિટર્ન્સ જુઓ"),
            "punjabi" to listOf("ਪੋਰਟਫੋਲੀਓ ਦੇਖੋ", "SIP ਸਥਿਤੀ", "ਨਵੀਂ SIP ਸ਼ੁਰੂ ਕਰੋ", "ਫੰਡ ਰਿਡੀਮ", "ਰਿਟਰਨ ਦੇਖੋ")
        ),
        "transaction" to mapOf(
            "english" to listOf("Track transaction", "Payment failed?", "Recent transactions", "Download receipt", "Refund status"),
            "hindi" to listOf("ट्रांजैक्शन ट्रैक करें", "पेमेंट फेल क्यों?", "हाल के लेन-देन", "रसीद डाउनलोड", "रिफंड स्थिति"),
            "tamil" to listOf("பரிவர்த்தனை ட்ராக்", "பேமெண்ட் ஃபெயில்?", "சமீபத்திய பரிவர்த்தனை", "ரசீது டவுன்லோட்", "ரீஃபண்ட் நிலை"),
            "telugu" to listOf("ట్రాన్సాక్షన్ ట్రాక్", "పేమెంట్ ఫెయిల్?", "ఇటీవలి లావాదేవీలు", "రసీదు డౌన్‌లోడ్", "రీఫండ్ స్థితి"),
            "kannada" to listOf("ವಹಿವಾಟು ಟ್ರ್ಯಾಕ್", "ಪಾವತಿ ವಿಫಲ?", "ಇತ್ತೀಚಿನ ವಹಿವಾಟು", "ರಶೀದಿ ಡೌನ್‌ಲೋಡ್", "ಮರುಪಾವತಿ ಸ್ಥಿತಿ"),
            "malayalam" to listOf("ട്രാൻസാക്ഷൻ ട്രാക്ക്", "പേയ്‌മെന്റ് ഫെയിൽ?", "സമീപകാല ഇടപാടുകൾ", "രസീത് ഡൗൺലോഡ്", "റീഫണ്ട് സ്റ്റാറ്റസ്"),
            "bengali" to listOf("লেনদেন ট্র্যাক", "পেমেন্ট ফেইল?", "সাম্প্রতিক লেনদেন", "রসিদ ডাউনলোড", "রিফান্ড স্ট্যাটাস"),
            "marathi" to listOf("व्यवहार ट्रॅक", "पेमेंट फेल?", "अलीकडील व्यवहार", "पावती डाउनलोड", "रिफंड स्थिती"),
            "gujarati" to listOf("વ્યવહાર ટ્રેક", "પેમેન્ટ ફેલ?", "તાજેતરના વ્યવહારો", "રસીદ ડાઉનલોડ", "રિફંડ સ્થિતિ"),
            "punjabi" to listOf("ਲੈਣ-ਦੇਣ ਟ੍ਰੈਕ", "ਪੇਮੈਂਟ ਫੇਲ?", "ਤਾਜ਼ਾ ਲੈਣ-ਦੇਣ", "ਰਸੀਦ ਡਾਊਨਲੋਡ", "ਰਿਫੰਡ ਸਥਿਤੀ")
        ),
        "account" to mapOf(
            "english" to listOf("Check balance", "Update mobile", "Account statement", "Reset PIN", "Unlock account"),
            "hindi" to listOf("बैलेंस चेक करें", "मोबाइल अपडेट", "स्टेटमेंट डाउनलोड", "PIN रीसेट", "अकाउंट अनलॉक"),
            "tamil" to listOf("பேலன்ஸ் சரிபார்க்க", "மொபைல் அப்டேட்", "ஸ்டேட்மெண்ட் டவுன்லோட்", "PIN ரீசெட்", "அக்கவுண்ட் அன்லாக்"),
            "telugu" to listOf("బ్యాలెన్స్ చెక్", "మొబైల్ అప్‌డేట్", "స్టేట్‌మెంట్ డౌన్‌లోడ్", "PIN రీసెట్", "అకౌంట్ అన్‌లాక్"),
            "kannada" to listOf("ಬ್ಯಾಲೆನ್ಸ್ ಚೆಕ್", "ಮೊಬೈಲ್ ಅಪ್‌ಡೇಟ್", "ಸ್ಟೇಟ್‌ಮೆಂಟ್ ಡೌನ್‌ಲೋಡ್", "PIN ರೀಸೆಟ್", "ಖಾತೆ ಅನ್‌ಲಾಕ್"),
            "malayalam" to listOf("ബാലൻസ് ചെക്ക്", "മൊബൈൽ അപ്‌ഡേറ്റ്", "സ്റ്റേറ്റ്‌മെന്റ് ഡൗൺലോഡ്", "PIN റീസെറ്റ്", "അക്കൗണ്ട് അൺലോക്ക്"),
            "bengali" to listOf("ব্যালেন্স চেক", "মোবাইল আপডেট", "স্টেটমেন্ট ডাউনলোড", "PIN রিসেট", "অ্যাকাউন্ট আনলক"),
            "marathi" to listOf("बॅलन्स तपासा", "मोबाईल अपडेट", "स्टेटमेंट डाउनलोड", "PIN रीसेट", "खाते अनलॉक"),
            "gujarati" to listOf("બેલેન્સ ચેક", "મોબાઈલ અપડેટ", "સ્ટેટમેન્ટ ડાઉનલોડ", "PIN રીસેટ", "એકાઉન્ટ અનલોક"),
            "punjabi" to listOf("ਬੈਲੇਂਸ ਚੈੱਕ", "ਮੋਬਾਈਲ ਅੱਪਡੇਟ", "ਸਟੇਟਮੈਂਟ ਡਾਊਨਲੋਡ", "PIN ਰੀਸੈੱਟ", "ਖਾਤਾ ਅਨਲੌਕ")
        ),
        "escalation" to mapOf(
            "english" to listOf("Talk to agent", "File complaint", "Report fraud", "Urgent help needed", "Call me back"),
            "hindi" to listOf("एजेंट से बात करें", "शिकायत दर्ज करें", "धोखाधड़ी रिपोर्ट", "तुरंत मदद चाहिए", "मुझे कॉल करें"),
            "tamil" to listOf("ஏஜெண்ட் உடன் பேச", "புகார் தாக்கல்", "மோசடி புகார்", "அவசர உதவி", "என்னை அழைக்கவும்"),
            "telugu" to listOf("ఏజెంట్‌తో మాట్లాడండి", "ఫిర్యాదు చేయండి", "మోసం నివేదించండి", "అత్యవసర సహాయం", "నాకు కాల్ చేయండి"),
            "kannada" to listOf("ಏಜೆಂಟ್ ಜೊತೆ ಮಾತನಾಡಿ", "ದೂರು ದಾಖಲಿಸಿ", "ವಂಚನೆ ವರದಿ", "ತುರ್ತು ಸಹಾಯ", "ನನಗೆ ಕಾಲ್ ಮಾಡಿ"),
            "malayalam" to listOf("ഏജന്റുമായി സംസാരിക്കുക", "പരാതി രജിസ്റ്റർ", "തട്ടിപ്പ് റിപ്പോർട്ട്", "അടിയന്തിര സഹായം", "എന്നെ വിളിക്കുക"),
            "bengali" to listOf("এজেন্টের সাথে কথা বলুন", "অভিযোগ দায়ের", "জালিয়াতি রিপোর্ট", "জরুরি সাহায্য", "আমাকে কল করুন"),
            "marathi" to listOf("एजंटशी बोला", "तक्रार नोंदवा", "फसवणूक अहवाल", "तात्काळ मदत", "मला कॉल करा"),
            "gujarati" to listOf("એજન્ટ સાથે વાત કરો", "ફરિયાદ નોંધાવો", "છેતરપિંડી રિપોર્ટ", "તાત્કાલિક મદદ", "મને કૉલ કરો"),
            "punjabi" to listOf("ਏਜੰਟ ਨਾਲ ਗੱਲ ਕਰੋ", "ਸ਼ਿਕਾਇਤ ਦਰਜ ਕਰੋ", "ਧੋਖਾਧੜੀ ਰਿਪੋਰਟ", "ਜ਼ਰੂਰੀ ਮਦਦ", "ਮੈਨੂੰ ਕਾਲ ਕਰੋ")
        )
    )

    private val defaultSuggestions = mapOf(
        "english" to listOf("Check KYC status", "View portfolio", "Track transaction", "Contact support"),
        "hindi" to listOf("KYC स्थिति जांचें", "पोर्टफोलियो देखें", "ट्रांजैक्शन ट्रैक करें", "सहायता संपर्क"),
        "tamil" to listOf("KYC நிலை சரிபார்க்க", "போர்ட்ஃபோலியோ பார்க்க", "பரிவர்த்தனை ட்ராக்", "ஆதரவு தொடர்பு"),
        "telugu" to listOf("KYC స్థితి చూడండి", "పోర్ట్‌ఫోలియో చూడండి", "ట్రాన్సాక్షన్ ట్రాక్", "సపోర్ట్ సంప్రదించండి"),
        "kannada" to listOf("KYC ಸ್ಥಿತಿ ಪರಿಶೀಲಿಸಿ", "ಪೋರ್ಟ್‌ಫೋಲಿಯೋ ನೋಡಿ", "ವಹಿವಾಟು ಟ್ರ್ಯಾಕ್", "ಬೆಂಬಲ ಸಂಪರ್ಕಿಸಿ"),
        "malayalam" to listOf("KYC സ്റ്റാറ്റസ്", "പോർട്ട്‌ഫോളിയോ കാണുക", "ട്രാൻസാക്ഷൻ ട്രാക്ക്", "സപ്പോർട്ട് ബന്ധപ്പെടുക"),
        "bengali" to listOf("KYC স্ট্যাটাস", "পোর্টফোলিও দেখুন", "লেনদেন ট্র্যাক", "সাপোর্ট যোগাযোগ"),
        "marathi" to listOf("KYC स्थिती", "पोर्टफोलिओ पहा", "व्यवहार ट्रॅक", "सपोर्ट संपर्क"),
        "gujarati" to listOf("KYC સ્થિતિ", "પોર્ટફોલિયો જુઓ", "વ્યવહાર ટ્રેક", "સપોર્ટ સંપર્ક"),
        "punjabi" to listOf("KYC ਸਥਿਤੀ", "ਪੋਰਟਫੋਲੀਓ ਦੇਖੋ", "ਲੈਣ-ਦੇਣ ਟ੍ਰੈਕ", "ਸਪੋਰਟ ਸੰਪਰਕ")
    )

    fun getSuggestions(intentCategory: String?, language: String, kycStatus: String? = null): List<String> {
        val lang = language.lowercase()
        val category = intentCategory?.lowercase() ?: "default"

        // If KYC intent and status is provided, return context-aware suggestions
        if (category == "kyc" && kycStatus != null) {
            return getKycStatusSuggestions(kycStatus.lowercase(), lang)
        }

        return suggestions[category]?.get(lang)
            ?: defaultSuggestions[lang]
            ?: defaultSuggestions["english"]!!
    }

    private fun getKycStatusSuggestions(status: String, language: String): List<String> {
        return when (status) {
            "pending" -> when (language) {
                "tamil" -> listOf("ஆவணங்கள் பதிவேற்றம்", "PAN புதுப்பிக்க", "ஆதார் இணைக்க", "புகைப்படுத்தம் செய்ய", "KYC நிலை சரிபார்க்க")
                "hindi" -> listOf("दस्तावेज़ अपलोड करें", "PAN अपडेट करें", "आधार लिंक करें", "सत्यापन करें", "KYC स्थिति जांचें")
                "english" -> listOf("Upload documents", "Update PAN", "Link Aadhaar", "Complete verification", "Check KYC status")
                else -> listOf("Upload documents", "Update PAN", "Link Aadhaar", "Complete verification", "Check KYC status")
            }
            "rejected" -> when (language) {
                "tamil" -> listOf("ஆவணங்கள் மீண்டும் பதிவேற்றம்", "PAN சரிசெய்ய", "பெயர் மாற்றம் புகார்", "ஆதார் சரிசெய்ய", "ஏன் நிராகரிக்கப்பட்டது?")
                "hindi" -> listOf("दस्तावेज़ फिर से जमा करें", "PAN सुधारें", "नाम मिसमैच शिकायत", "आधार सुधारें", "क्यों अस्वीकृत किया गया?")
                "english" -> listOf("Re-submit documents", "Correct PAN", "Name mismatch complaint", "Correct Aadhaar", "Why was it rejected?")
                else -> listOf("Re-submit documents", "Correct PAN", "Name mismatch complaint", "Correct Aadhaar", "Why was it rejected?")
            }
            "verified" -> when (language) {
                "tamil" -> listOf("ஆவணங்கள் புதுப்பிக்க", "முகவரி மாற்றம்", "பெயர் மாற்றம்", "மொபைல் மாற்றம்", "KYC நிலை சரிபார்க்க")
                "hindi" -> listOf("दस्तावेज़ अपडेट करें", "पता बदलें", "नाम बदलें", "मोबाइल बदलें", "KYC स्थिति जांचें")
                "english" -> listOf("Update documents", "Change address", "Change name", "Update mobile", "Check KYC status")
                else -> listOf("Update documents", "Change address", "Change name", "Update mobile", "Check KYC status")
            }
            else -> suggestions["kyc"]?.get(language) ?: defaultSuggestions[language] ?: defaultSuggestions["english"]!!
        }
    }
}
