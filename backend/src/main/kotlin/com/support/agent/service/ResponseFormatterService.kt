package com.support.agent.service

import com.support.agent.model.ConversationMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ResponseFormatterService(
    private val claudeService: ClaudeService
) {
    private val logger = LoggerFactory.getLogger(ResponseFormatterService::class.java)

    fun format(
        userMessage: String,
        detectedLanguage: String,
        data: Map<String, Any>,
        conversationHistory: List<ConversationMessage>
    ): String {
        logger.info("Formatting response in $detectedLanguage with data: $data")
        
        return try {
            claudeService.formatResponse(userMessage, detectedLanguage, data, conversationHistory)
        } catch (e: Exception) {
            logger.error("Response formatting failed: ${e.message}", e)
            getFallbackResponse(detectedLanguage, data)
        }
    }

    private fun getFallbackResponse(language: String, data: Map<String, Any>): String {
        val dataStr = data.entries.joinToString(", ") { "${it.key}: ${it.value}" }
        
        return when (language.lowercase()) {
            "hindi" -> "आपकी जानकारी: $dataStr। कृपया दोबारा प्रयास करें या हमारी हेल्पलाइन से संपर्क करें।"
            "tamil" -> "உங்கள் தகவல்: $dataStr. மீண்டும் முயற்சிக்கவும் அல்லது எங்கள் உதவி எண்ணை தொடர்பு கொள்ளவும்."
            "telugu" -> "మీ సమాచారం: $dataStr. దయచేసి మళ్ళీ ప్రయత్నించండి లేదా మా హెల్ప్‌లైన్‌ను సంప్రదించండి."
            "kannada" -> "ನಿಮ್ಮ ಮಾಹಿತಿ: $dataStr. ದಯವಿಟ್ಟು ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ ಅಥವಾ ನಮ್ಮ ಸಹಾಯವಾಣಿಯನ್ನು ಸಂಪರ್ಕಿಸಿ."
            else -> "Your information: $dataStr. Please try again or contact our helpline."
        }
    }
}
