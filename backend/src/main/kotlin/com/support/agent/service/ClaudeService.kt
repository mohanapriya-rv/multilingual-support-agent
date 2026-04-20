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
    @Value("\${claude.model:claude-haiku-4-5}") private val model: String,
    @Value("\${claude.max-tokens:1024}") private val maxTokens: Int,
    @Value("\${claude.timeout-seconds:30}") private val timeoutSeconds: Long,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(ClaudeService::class.java)
    
    private val webClient = WebClient.builder()
        .baseUrl("https://api.anthropic.com")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    companion object {
        const val INTENT_EXTRACTION_SYSTEM_PROMPT = """You are an intent extractor for a fintech support system. Read the user message and return ONLY a valid JSON object with these exact fields:
- detected_language: one of "hindi", "tamil", "telugu", "kannada", "english"
- intent_category: one of "kyc", "transaction", "account", "out_of_scope"
- intent_type: a specific type like "status_check", "failed_payment", "balance_check", "refund_status", etc.
- confidence: a number between 0 and 1
- entities: object with user_id, transaction_id, merchant, amount (null if not mentioned)
- requires_escalation: boolean (true if fraud mentioned, legal threats, permanent block, anger detected)
- escalation_reason: string or null
- is_in_scope: boolean (false for loans, investments, insurance, unrelated queries)
- out_of_scope_reason: string or null

Do not return any text outside the JSON. Do not add explanation or markdown code fences.

Intent categories:
- kyc: KYC verification, documents, status
- transaction: payments, refunds, failed transfers, transaction history
- account: balance, blocked account, profile changes
- out_of_scope: loans, investments, insurance, unrelated queries

Escalate if: fraud mentioned, legal threats, permanent account block, anger across multiple turns, asking about someone else's account."""

        const val RESPONSE_FORMATTING_SYSTEM_PROMPT = """You are a warm, helpful customer support agent for an Indian fintech app serving users across India.

CRITICAL RULES:
1. You must ALWAYS respond in the exact same language and script the user wrote in:
   - If they wrote in Hindi (Devanagari script), respond in Hindi using Devanagari
   - If Tamil script, respond in Tamil
   - If Telugu script, respond in Telugu
   - If Kannada script, respond in Kannada
   - If English, respond in English

2. Be empathetic and culturally warm - Indian users expect this from support

3. Use the data provided to give accurate, specific answers. Never make up data.

4. If the data shows an error or problem, explain it clearly and tell the user what they can do next.

5. Keep responses concise but helpful (2-4 sentences typically).

6. Use appropriate greetings based on language (नमस्ते for Hindi, வணக்கம் for Tamil, etc.)

7. Always provide specific details from the data (amounts, dates, reasons) when available."""
    }

    fun extractIntent(userMessage: String, conversationHistory: List<ConversationMessage>): String {
        val messages = buildMessagesForExtraction(userMessage, conversationHistory)
        return callClaude(INTENT_EXTRACTION_SYSTEM_PROMPT, messages, maxTokens = 500)
    }

    fun formatResponse(
        userMessage: String,
        detectedLanguage: String,
        data: Map<String, Any>,
        conversationHistory: List<ConversationMessage>
    ): String {
        if (apiKey.isBlank()) {
            logger.warn("Claude API key not configured, returning mock formatted response")
            return getMockFormattedResponse(detectedLanguage, data)
        }
        
        val dataString = formatDataForPrompt(data)
        val prompt = """User question: $userMessage

Detected language: $detectedLanguage

Data from database:
$dataString

Please respond to the user's question in $detectedLanguage using this data."""

        val messages = conversationHistory.takeLast(4).map { 
            mapOf("role" to it.role, "content" to it.content) 
        } + listOf(mapOf("role" to "user", "content" to prompt))
        
        return callClaude(RESPONSE_FORMATTING_SYSTEM_PROMPT, messages, maxTokens = maxTokens)
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
