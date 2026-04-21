package com.support.agent.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.support.agent.model.ConversationMessage
import com.support.agent.model.ExtractedIntent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class IntentExtractorService(
    private val claudeService: ClaudeService,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(IntentExtractorService::class.java)

    fun extract(userMessage: String, conversationHistory: List<ConversationMessage>): ExtractedIntent {
        return try {
            val response = claudeService.extractIntent(userMessage, conversationHistory)
            logger.info("Claude intent extraction response: $response")
            parseIntentResponse(response)
        } catch (e: Exception) {
            logger.error("Intent extraction failed: ${e.message}")
            createFallbackIntent(userMessage)
        }
    }

    private fun parseIntentResponse(response: String): ExtractedIntent {
        val cleanedResponse = response
            .replace("```json", "")
            .replace("```", "")
            .trim()
        
        return try {
            objectMapper.readValue(cleanedResponse, ExtractedIntent::class.java)
        } catch (e: Exception) {
            logger.error("Failed to parse intent JSON: $cleanedResponse", e)
            createFallbackIntent("")
        }
    }

    private fun createFallbackIntent(userMessage: String): ExtractedIntent {
        val detectedLanguage = detectLanguageFromUnicode(userMessage)
        return ExtractedIntent(
            detectedLanguage = detectedLanguage,
            intentCategory = "out_of_scope",
            intentType = "unknown",
            confidence = 0.0,
            isInScope = false,
            outOfScopeReason = "Could not understand the query"
        )
    }

    private fun detectLanguageFromUnicode(text: String): String {
        val languageCounts = mutableMapOf(
            "english" to 0,
            "hindi" to 0,
            "tamil" to 0,
            "telugu" to 0,
            "kannada" to 0,
            "malayalam" to 0,
            "bengali" to 0,
            "marathi" to 0,
            "gujarati" to 0,
            "punjabi" to 0
        )

        for (char in text) {
            val codePoint = char.code
            when {
                codePoint in 0x0900..0x097F -> languageCounts["hindi"] = languageCounts["hindi"]!! + 1
                codePoint in 0x0B80..0x0BFF -> languageCounts["tamil"] = languageCounts["tamil"]!! + 1
                codePoint in 0x0C00..0x0C7F -> languageCounts["telugu"] = languageCounts["telugu"]!! + 1
                codePoint in 0x0C80..0x0CFF -> languageCounts["kannada"] = languageCounts["kannada"]!! + 1
                codePoint in 0x0D00..0x0D7F -> languageCounts["malayalam"] = languageCounts["malayalam"]!! + 1
                codePoint in 0x0980..0x09FF -> languageCounts["bengali"] = languageCounts["bengali"]!! + 1
                codePoint in 0x0900..0x097F -> languageCounts["marathi"] = languageCounts["marathi"]!! + 1
                codePoint in 0x0A80..0x0AFF -> languageCounts["gujarati"] = languageCounts["gujarati"]!! + 1
                codePoint in 0x0A00..0x0A7F -> languageCounts["punjabi"] = languageCounts["punjabi"]!! + 1
                codePoint in 0x0000..0x007F -> languageCounts["english"] = languageCounts["english"]!! + 1
            }
        }

        // Default to English if no specific script detected
        val maxLang = languageCounts.maxByOrNull { it.value }?.key ?: "english"
        // If English count is 0 but others are also 0, default to English
        if (languageCounts["english"]!! > 0) return "english"
        return maxLang
    }
}
