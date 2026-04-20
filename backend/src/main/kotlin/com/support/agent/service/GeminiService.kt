package com.support.agent.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.support.agent.model.ConversationMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Service
class GeminiService(
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(GeminiService::class.java)

    @Value("\${gemini.api-key:}")
    private lateinit var apiKey: String

    @Value("\${gemini.model:gemini-1.5-flash}")
    private lateinit var model: String

    @Value("\${gemini.timeout-seconds:30}")
    private var timeoutSeconds: Long = 30

    private val webClient = WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1beta")
        .build()

    companion object {
        private val INTENT_EXTRACTION_PROMPT = """
You are an intent extraction system for a FINTECH & MUTUAL FUNDS customer support agent.
Analyze the user's message and extract structured information.

IMPORTANT: You MUST respond with ONLY valid JSON, no other text.

Supported languages: Hindi, Tamil, Telugu, Kannada, English

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

        private val RESPONSE_FORMATTING_PROMPT = """
You are a friendly multilingual FINTECH & MUTUAL FUNDS customer support agent.
You work for a financial services company helping users with investments, SIPs, and transactions.

Rules:
1. Respond ONLY in the detected language (Hindi/Tamil/Telugu/Kannada/English)
2. Be helpful, warm, and professional - this is about their money!
3. Use appropriate Indian greetings (नमस्ते, வணக்கம், నమస్కారం, ನಮಸ್ಕಾರ)
4. Format currency in Indian format: ₹1,00,000 (with commas)
5. Format percentages clearly: 12.5% returns
6. Keep responses concise but informative
7. For investments, always mention any risks if applicable
8. End with an offer to help further
9. Use 🎯📈💰 emojis sparingly for financial context
""".trimIndent()
    }

    fun extractIntent(userMessage: String, conversationHistory: List<ConversationMessage>): String {
        val prompt = buildString {
            append(INTENT_EXTRACTION_PROMPT)
            append("\n\nConversation history:\n")
            conversationHistory.takeLast(4).forEach {
                append("${it.role}: ${it.content}\n")
            }
            append("\nUser's current message: $userMessage")
            append("\n\nRespond with JSON only:")
        }

        return callGemini(prompt)
    }

    fun formatResponse(
        userMessage: String,
        detectedLanguage: String,
        data: Map<String, Any>,
        conversationHistory: List<ConversationMessage>
    ): String {
        val dataString = formatDataForPrompt(data)
        
        val prompt = buildString {
            append(RESPONSE_FORMATTING_PROMPT)
            append("\n\nDetected language: $detectedLanguage")
            append("\nUser's question: $userMessage")
            append("\n\nData from database:\n$dataString")
            append("\n\nRespond naturally in $detectedLanguage:")
        }

        return callGemini(prompt)
    }

    private fun callGemini(prompt: String): String {
        if (apiKey.isBlank()) {
            logger.error("Gemini API key not configured!")
            throw IllegalStateException("Gemini API key not configured. Set GEMINI_API_KEY environment variable.")
        }

        val requestBody = mapOf(
            "contents" to listOf(
                mapOf(
                    "parts" to listOf(
                        mapOf("text" to prompt)
                    )
                )
            ),
            "generationConfig" to mapOf(
                "temperature" to 0.7,
                "maxOutputTokens" to 1024
            )
        )

        return try {
            val response = webClient.post()
                .uri("/models/$model:generateContent?key=$apiKey")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String::class.java)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .block()

            parseGeminiResponse(response)
        } catch (e: Exception) {
            logger.error("Gemini API call failed: ${e.message}", e)
            throw RuntimeException("Failed to get response from Gemini: ${e.message}")
        }
    }

    private fun parseGeminiResponse(response: String?): String {
        if (response == null) return ""
        
        return try {
            val jsonNode: JsonNode = objectMapper.readTree(response)
            val text = jsonNode
                .path("candidates")
                .firstOrNull()
                ?.path("content")
                ?.path("parts")
                ?.firstOrNull()
                ?.path("text")
                ?.asText() ?: ""
            
            // Clean up JSON responses (remove markdown code blocks if present)
            text.replace("```json", "")
                .replace("```", "")
                .trim()
        } catch (e: Exception) {
            logger.error("Failed to parse Gemini response: ${e.message}")
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
}
