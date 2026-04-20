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
    @Value("\${claude.model:claude-3-haiku-20240307}") private val model: String,
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

IMPORTANT: You MUST respond with ONLY valid JSON, no other text.

Supported languages: $supportedLanguages

=== FINTECH & MUTUAL FUNDS SUPPORTED QUERIES (20+ types) ===

1. ACCOUNT MANAGEMENT:
   - account_balance: "मेरा बैलेंस कितना है?", "My wallet balance"
   - account_status: "मेरा अकाउंट ब्लॉक क्यों है?", "Account blocked reason"
   - account_statement: "मुझे स्टेटमेंट चाहिए", "Send account statement"
   - update_profile: "मोबाइल नंबर बदलना है", "Update email/phone"

2. MUTUAL FUNDS:
   - portfolio_value: "मेरा पोर्टफोलियो कितना है?", "Show my investments"
   - sip_status: "मेरा SIP status क्या है?", "SIP active or not"
   - sip_start: "SIP शुरू करना है", "Start new SIP"
   - sip_stop: "SIP बंद करना है", "Stop/pause SIP"
   - sip_modify: "SIP amount बदलना है", "Change SIP amount"
   - fund_nav: "NAV क्या है?", "Current NAV of fund"
   - fund_returns: "रिटर्न कितना मिला?", "Show fund returns"
   - fund_recommend: "कौन सा फंड अच्छा है?", "Suggest best fund"
   - redemption: "पैसे निकालने हैं", "Withdraw/redeem funds"
   - switch_fund: "फंड स्विच करना है", "Switch between funds"

3. TRANSACTIONS:
   - recent_transactions: "हाल के ट्रांजैक्शन दिखाओ", "Recent transactions"
   - failed_payment: "पेमेंट फेल हो गया", "Payment failed"
   - refund_status: "रिफंड कब आएगा?", "Refund status"
   - transaction_details: "ट्रांजैक्शन डिटेल्स", "Transaction details"

4. KYC & COMPLIANCE:
   - kyc_status: "KYC status क्या है?", "Check KYC"
   - kyc_update: "KYC अपडेट करना है", "Update KYC documents"
   - pan_link: "PAN link करना है", "Link PAN card"
   - aadhaar_link: "आधार link करना है", "Link Aadhaar"

5. TAX & REPORTS:
   - capital_gains: "कैपिटल गेन रिपोर्ट", "Capital gains statement"
   - tax_statement: "टैक्स स्टेटमेंट चाहिए", "Tax saving proof"
   - annual_report: "सालाना रिपोर्ट", "Annual investment report"

6. SUPPORT & ESCALATION:
   - fraud_report: "धोखाधड़ी हुई है", "Fraud/unauthorized transaction"
   - complaint: "शिकायत करनी है", "File complaint"
   - speak_agent: "एजेंट से बात करनी है", "Talk to human"

Response format (JSON only):
{
  "detected_language": "hindi|tamil|telugu|kannada|english",
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
        
        return when {
            lowerMessage.contains("kyc") || lowerMessage.contains("केवाईसी") || lowerMessage.contains("status") -> 
                """{"detected_language":"hindi","intent_category":"kyc","intent_type":"status_check","confidence":0.9,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
            
            lowerMessage.contains("payment") || lowerMessage.contains("fail") || lowerMessage.contains("पेमेंट") || lowerMessage.contains("transaction") ->
                """{"detected_language":"hindi","intent_category":"transaction","intent_type":"failed_payment","confidence":0.95,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
            
            lowerMessage.contains("balance") || lowerMessage.contains("बैलेंस") || lowerMessage.contains("account") ->
                """{"detected_language":"hindi","intent_category":"account","intent_type":"balance_check","confidence":0.95,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
            
            lowerMessage.contains("fraud") || lowerMessage.contains("धोखा") ->
                """{"detected_language":"hindi","intent_category":"account","intent_type":"fraud_report","confidence":0.9,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":true,"escalation_reason":"Fraud mentioned by user","is_in_scope":true,"out_of_scope_reason":null}"""
            
            else ->
                """{"detected_language":"english","intent_category":"account","intent_type":"general_query","confidence":0.7,"entities":{"user_id":null,"transaction_id":null,"merchant":null,"amount":null},"requires_escalation":false,"escalation_reason":null,"is_in_scope":true,"out_of_scope_reason":null}"""
        }
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
