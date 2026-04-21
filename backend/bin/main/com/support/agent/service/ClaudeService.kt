package com.support.agent.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.support.agent.model.ConversationMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Duration

@Service
class ClaudeService(
    @Value("\${claude.api-key:}") private val apiKey: String,
    @Value("\${claude.model:claude-sonnet-4-20250514}") private val model: String,
    @Value("\${claude.max-tokens:1024}") private val maxTokens: Int,
    @Value("\${claude.timeout-seconds:30}") private val timeoutSeconds: Long,
    private val objectMapper: ObjectMapper,
    private val languageService: LanguageService
) {
    private val logger = LoggerFactory.getLogger(ClaudeService::class.java)
    
    private val webClient = WebClient.builder()
        .baseUrl("https://api.anthropic.com")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    private fun buildIntentExtractionPrompt(): String {
        val supportedLanguages = languageService.getSupportedLanguagesForPrompt()
        return """
You are an intent extraction system for a FINTECH & MUTUAL FUNDS customer support agent.
Analyze the user's message and extract structured information.

CRITICAL: Detect the ACTUAL language of the user's input message. 
- If the message is in English (Latin script), set detected_language to "english"
- If the message is in Hindi (Devanagari script), set detected_language to "hindi"
- If the message is in Tamil (Tamil script), set detected_language to "tamil"
- If the message is in Telugu (Telugu script), set detected_language to "telugu"
- If the message is in Kannada (Kannada script), set detected_language to "kannada"
- If the message is in Malayalam (Malayalam script), set detected_language to "malayalam"
- If the message is in Bengali (Bengali script), set detected_language to "bengali"
- If the message is in Marathi (Devanagari script), set detected_language to "marathi"
- If the message is in Gujarati (Gujarati script), set detected_language to "gujarati"
- If the message is in Punjabi (Gurmukhi script), set detected_language to "punjabi"
- Default to "english" if uncertain

IMPORTANT: You MUST respond with ONLY valid JSON, no other text.

Supported languages: $supportedLanguages

=== FINTECH & MUTUAL FUNDS SUPPORTED QUERIES (20+ types) ===

1. ACCOUNT MANAGEMENT:
   - account_balance: "My wallet balance", "मेरा बैलेंस कितना है?"
   - account_status: "Account blocked reason", "मेरा अकाउंट ब्लॉक क्यों है?"
   - account_statement: "Send account statement", "मुझे स्टेटमेंट चाहिए"
   - update_profile: "Update email/phone", "मोबाइल नंबर बदलना है"

2. MUTUAL FUNDS:
   - portfolio_value: "Show my investments", "मेरा पोर्टफोलियो कितना है?", "My portfolio value", "Total investment", "How much have I invested", "Investment details"
   - sip_status: "SIP active or not", "मेरा SIP status क्या है?", "Check my SIP", "SIP running or not", "Is my SIP active", "SIP details"
   - sip_start: "Start new SIP", "SIP शुरू करना है", "I want to start SIP", "Begin SIP investment", "Create new SIP", "Setup SIP"
   - sip_stop: "Stop/pause SIP", "SIP बंद करना है", "Cancel my SIP", "Stop SIP investment", "Pause SIP", "Terminate SIP"
   - sip_modify: "Change SIP amount", "SIP amount बदलना है", "Increase SIP amount", "Modify SIP", "Change SIP date", "Update SIP"
   - fund_nav: "Current NAV of fund", "NAV क्या है?", "What is NAV", "Check NAV value", "Fund NAV today"
   - fund_returns: "Show fund returns", "रिटर्न कितना मिला?", "How much return", "Fund performance", "Investment returns", "Profit or loss"
   - fund_recommend: "Suggest best fund", "कौन सा फंड अच्छा है?", "Which fund to invest", "Best mutual funds", "Fund recommendations", "Top performing funds"
   - redemption: "Withdraw/redeem funds", "पैसे निकालने हैं", "Redeem my investment", "Withdraw money", "Get my money back", "Exit fund"
   - switch_fund: "Switch between funds", "फंड स्विच करना है", "Switch my investment", "Move to another fund", "Fund switch"

3. TRANSACTIONS:
   - recent_transactions: "हाल के ट्रांजैक्शन दिखाओ", "Recent transactions", "Show my transactions", "Transaction history"
   - failed_payment: "पेमेंट फेल हो गया", "Payment failed", "My transaction failed", "Transaction declined", "Payment not processed", "1.5 lakh transaction failed"
   - refund_status: "रिफंड कब आएगा?", "Refund status", "Where is my refund", "Refund pending", "When will I get my money back"
   - transaction_details: "ट्रांजैक्शन डिटेल्स", "Transaction details", "Check transaction status", "Track my payment", "Payment stuck", "Debited but not credited"
   - refund_initiated: "Refund initiated", "Money refunded", "Amount credited back", "ट्रांजैक्शन डिटेल्स"

4. KYC & COMPLIANCE:
   - kyc_status: "Check KYC", "KYC status क्या है?", "Is my KYC done", "KYC verification status", "KYC pending or complete", "Check verification"
   - kyc_update: "Update KYC documents", "KYC अपडेट करना है", "Upload KYC documents", "Submit KYC", "Complete KYC", "Update my KYC"
   - pan_link: "Link PAN card", "PAN link करना है", "Add PAN number", "Update PAN details", "Link PAN to account"
   - aadhaar_link: "Link Aadhaar", "आधार link करना है", "Add Aadhaar", "Link Aadhaar card", "Verify Aadhaar"
   - kyc_rejection: "KYC rejected", "KYC failed", "Why KYC rejected", "KYC not approved"

5. TAX & REPORTS:
   - capital_gains: "Capital gains statement", "कैपिटल गेन रिपोर्ट"
   - tax_statement: "Tax saving proof", "टैक्स स्टेटमेंट चाहिए"
   - annual_report: "Annual investment report", "सालाना रिपोर्ट"

6. SUPPORT & ESCALATION:
   - fraud_report: "Fraud/unauthorized transaction", "धोखाधड़ी हुई है"
   - complaint: "File complaint", "शिकायत करनी है"
   - speak_agent: "Talk to human", "एजेंट से बात करनी है"

Response format (JSON only):
{
  "detected_language": "english|hindi|tamil|telugu|kannada|malayalam|bengali|marathi|gujarati|punjabi",
  "intent_category": "account|mutual_fund|transaction|kyc|tax|escalation|unknown",
  "intent_type": "specific_intent_from_above",
  "confidence": 0.0-1.0,
  "entities": {
    "user_id": null,
    "fund_name": null,
    "sip_id": null,
    "transaction_id": null,
    "amount": null
  },
  "requires_escalation": false,
  "escalation_reason": null,
  "is_in_scope": true,
  "out_of_scope_reason": null
}
""".trimIndent()
    }

    private fun buildResponseFormattingPrompt(): String {
        val languages = languageService.getAllActiveLanguages()
        val greetings = languages.joinToString(", ") { "${it.greeting} (${it.name})" }
        val languageList = languages.joinToString("/") { it.name }
        
        return """
You are a friendly multilingual FINTECH & MUTUAL FUNDS customer support agent.
You work for a financial services company helping users with investments, SIPs, and transactions.

Rules:
1. Respond ONLY in the detected language ($languageList)
2. Be helpful, warm, and professional - this is about their money!
3. Use appropriate greetings based on language: $greetings
4. Format currency in Indian format: ₹1,00,000 (with commas)
5. Format percentages clearly: 12.5% returns
6. Keep responses concise but informative
7. For investments, always mention any risks if applicable
8. End with an offer to help further
9. Use 🎯📈💰 emojis sparingly for financial context
""".trimIndent()
    }

    fun extractIntent(userMessage: String, conversationHistory: List<ConversationMessage>): String {
        val messages = buildMessagesForExtraction(userMessage, conversationHistory)
        return callClaude(buildIntentExtractionPrompt(), messages, maxTokens = 500)
    }

    fun formatResponse(
        userMessage: String,
        detectedLanguage: String,
        data: Map<String, Any>,
        conversationHistory: List<ConversationMessage>,
        faqContext: String = ""
    ): String {
        if (apiKey.isBlank()) {
            logger.warn("Claude API key not configured, returning mock formatted response")
            return getMockFormattedResponse(detectedLanguage, data)
        }
        
        val dataString = formatDataForPrompt(data)
        val prompt = buildString {
            append("User question: $userMessage\n\n")
            append("Detected language: $detectedLanguage\n\n")
            if (faqContext.isNotBlank()) {
                append("FAQ Knowledge:\n$faqContext\n\n")
            }
            append("Data from database:\n$dataString\n\n")
            append("Please respond to the user's question in $detectedLanguage using this data.")
        }

        val messages = conversationHistory.takeLast(4).map { 
            mapOf("role" to it.role, "content" to it.content) 
        } + listOf(mapOf("role" to "user", "content" to prompt))
        
        return callClaude(buildResponseFormattingPrompt(), messages, maxTokens = maxTokens)
    }

    private fun buildMessagesForExtraction(
        userMessage: String, 
        conversationHistory: List<ConversationMessage>
    ): List<Map<String, String>> {
        val recentHistory = conversationHistory.takeLast(6)
        val historyMessages = recentHistory.map { mapOf("role" to it.role, "content" to it.content) }
        return historyMessages + listOf(mapOf("role" to "user", "content" to userMessage))
    }

    private fun callClaude(systemPrompt: String, messages: List<Map<String, String>>, maxTokens: Int): String {
        if (apiKey.isBlank()) {
            logger.warn("Claude API key not configured, returning mock response")
            return getMockResponse(messages.lastOrNull()?.get("content") ?: "")
        }

        val requestBody = mapOf(
            "model" to model,
            "max_tokens" to maxTokens,
            "system" to systemPrompt,
            "messages" to messages
        )

        return try {
            val response = webClient.post()
                .uri("/v1/messages")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono<String>()
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .block()

            parseClaudeResponse(response)
        } catch (e: Exception) {
            logger.error("Claude API call failed: ${e.message}", e)
            getMockResponse(messages.lastOrNull()?.get("content") ?: "")
        }
    }

    private fun parseClaudeResponse(response: String?): String {
        if (response == null) return ""
        return try {
            val jsonNode: JsonNode = objectMapper.readTree(response)
            jsonNode.path("content").firstOrNull()?.path("text")?.asText() ?: ""
        } catch (e: Exception) {
            logger.error("Failed to parse Claude response: ${e.message}")
            response
        }
    }

    private fun formatDataForPrompt(data: Map<String, Any>): String {
        if (data.isEmpty()) return "No data found for this query."
        
        return data.entries.joinToString("\n") { (key, value) ->
            val formattedKey = key.replace("_", " ").replaceFirstChar { it.uppercase() }
            "$formattedKey: $value"
        }
    }

    private fun getMockResponse(userMessage: String): String {
        val lowerMessage = userMessage.lowercase()
        
        // Detect language from input
        val detectedLanguage = detectLanguageFromInput(userMessage)
        
        return when {
            lowerMessage.contains("kyc") || lowerMessage.contains("केवाईसी") || lowerMessage.contains("status") -> 
                """{"detected_language":"$detectedLanguage","intent_category":"kyc","intent_type":"status_check","confidence":0.9,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
            
            lowerMessage.contains("payment") || lowerMessage.contains("fail") || lowerMessage.contains("failed") || 
            lowerMessage.contains("पेमेंट") || lowerMessage.contains("transaction") || lowerMessage.contains("transactions") ||
            lowerMessage.contains("lakh") || lowerMessage.contains("debit") || lowerMessage.contains("credit") ||
            lowerMessage.contains("refund") || lowerMessage.contains("stuck") || lowerMessage.contains("pending") ->
                """{"detected_language":"$detectedLanguage","intent_category":"transaction","intent_type":"failed_payment","confidence":0.95,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
            
            lowerMessage.contains("balance") || lowerMessage.contains("बैलेंस") || lowerMessage.contains("account") ||
            lowerMessage.contains("money") || lowerMessage.contains("amount") ->
                """{"detected_language":"$detectedLanguage","intent_category":"account","intent_type":"balance_check","confidence":0.95,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
            
            lowerMessage.contains("fraud") || lowerMessage.contains("धोखा") || lowerMessage.contains("unauthorized") ->
                """{"detected_language":"$detectedLanguage","intent_category":"account","intent_type":"fraud_report","confidence":0.9,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":true,"escalation_reason":"Fraud mentioned by user","is_in_scope":true,"out_of_scope_reason":null}"""
            
            lowerMessage.contains("sip") || lowerMessage.contains("fund") || lowerMessage.contains("investment") || 
            lowerMessage.contains("portfolio") || lowerMessage.contains("mutual") || lowerMessage.contains("redemption") ||
            lowerMessage.contains("nav") || lowerMessage.contains("return") ->
                """{"detected_language":"$detectedLanguage","intent_category":"mutual_fund","intent_type":"portfolio_value","confidence":0.95,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
            
            else ->
                """{"detected_language":"$detectedLanguage","intent_category":"account","intent_type":"general_query","confidence":0.7,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
        }
    }

    private fun detectLanguageFromInput(text: String): String {
        for (char in text) {
            val codePoint = char.code
            when {
                codePoint in 0x0900..0x097F -> return "hindi"
                codePoint in 0x0B80..0x0BFF -> return "tamil"
                codePoint in 0x0C00..0x0C7F -> return "telugu"
                codePoint in 0x0C80..0x0CFF -> return "kannada"
                codePoint in 0x0D00..0x0D7F -> return "malayalam"
                codePoint in 0x0980..0x09FF -> return "bengali"
                codePoint in 0x0A00..0x0A7F -> return "punjabi"
                codePoint in 0x0A80..0x0AFF -> return "gujarati"
            }
        }
        return "english"
    }

    private fun getMockFormattedResponse(language: String, data: Map<String, Any>): String {
        val dataStr = formatDataForPrompt(data)
        
        return when (language.lowercase()) {
            "hindi" -> "नमस्ते! 🙏 आपकी जानकारी:\n\n$dataStr\n\nक्या मैं आपकी और कोई मदद कर सकता हूँ?"
            "tamil" -> "வணக்கம்! 🙏 உங்கள் தகவல்:\n\n$dataStr\n\nவேறு ஏதாவது உதவி வேண்டுமா?"
            "telugu" -> "నమస్కారం! 🙏 మీ సమాచారం:\n\n$dataStr\n\nమరేదైనా సహాయం కావాలా?"
            "kannada" -> "ನಮಸ್ಕಾರ! 🙏 ನಿಮ್ಮ ಮಾಹಿತಿ:\n\n$dataStr\n\nಬೇರೆ ಏನಾದರೂ ಸಹಾಯ ಬೇಕೇ?"
            else -> "Hello! 🙏 Here's your information:\n\n$dataStr\n\nIs there anything else I can help you with?"
        }
    }
}
