package com.support.agent.service

import com.support.agent.model.ConversationMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ResponseFormatterService(
    private val geminiService: GeminiService,
    private val faqService: FAQService,
    private val languageService: LanguageService  // NEW: For dynamic fallback messages
) {
    private val logger = LoggerFactory.getLogger(ResponseFormatterService::class.java)

    fun format(
        userMessage: String,
        detectedLanguage: String,
        intentCategory: String,
        data: Map<String, Any>,
        conversationHistory: List<ConversationMessage>
    ): String {
        logger.info("Formatting response in $detectedLanguage with data: $data")
        
        return try {
            // Get relevant FAQs based on intent and message
            val relevantFaqs = faqService.getRelevantFAQs(intentCategory, userMessage, detectedLanguage)
            val faqContext = faqService.formatFAQsForPrompt(relevantFaqs, detectedLanguage)
            
            logger.info("Found ${relevantFaqs.size} relevant FAQs for category: $intentCategory")
            
            geminiService.formatResponse(userMessage, detectedLanguage, data, conversationHistory, faqContext)
        } catch (e: Exception) {
            logger.error("Response formatting failed: ${e.message}", e)
            getFallbackResponse(detectedLanguage, data)
        }
    }

    private fun getFallbackResponse(language: String, data: Map<String, Any>): String {
        val dataStr = data.entries.joinToString(", ") { "${it.key}: ${it.value}" }
        // Now uses dynamic fallback message from database
        return languageService.getFallbackMessage(language, dataStr)
    }
}
