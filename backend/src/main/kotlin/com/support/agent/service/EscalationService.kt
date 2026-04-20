package com.support.agent.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.support.agent.entity.Escalation
import com.support.agent.model.ConversationMessage
import com.support.agent.model.ExtractedIntent
import com.support.agent.repository.EscalationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EscalationService(
    private val escalationRepository: EscalationRepository,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(EscalationService::class.java)

    companion object {
        val ESCALATION_MESSAGES = mapOf(
            "hindi" to "आपका मुद्दा हमारी विशेषज्ञ टीम को भेज दिया गया है। हम 24 घंटे में आपसे संपर्क करेंगे। Ticket ID: {escalationId}",
            "tamil" to "உங்கள் பிரச்சனை எங்கள் நிபுணர் குழுவிற்கு அனுப்பப்பட்டது. 24 மணி நேரத்தில் தொடர்பு கொள்வோம். Ticket ID: {escalationId}",
            "telugu" to "మీ సమస్య మా నిపుణుల బృందానికి పంపబడింది. 24 గంటల్లో మీకు తెలియజేస్తాం. Ticket ID: {escalationId}",
            "kannada" to "ನಿಮ್ಮ ಸಮಸ್ಯೆಯನ್ನು ನಮ್ಮ ತಜ್ಞ ತಂಡಕ್ಕೆ ಕಳುಹಿಸಲಾಗಿದೆ. 24 ಗಂಟೆಗಳಲ್ಲಿ ಸಂಪರ್ಕಿಸುತ್ತೇವೆ. Ticket ID: {escalationId}",
            "english" to "Your issue has been escalated to our specialist team. We will contact you within 24 hours. Ticket ID: {escalationId}"
        )

        val OUT_OF_SCOPE_MESSAGES = mapOf(
            "hindi" to "मैं केवल KYC, लेनदेन और खाते से संबंधित प्रश्नों में मदद कर सकता हूं। अन्य सवालों के लिए कृपया हमारी हेल्पलाइन 1800-XXX-XXXX पर कॉल करें।",
            "tamil" to "KYC, பரிவர்த்தனைகள் மற்றும் கணக்கு தொடர்பான கேள்விகளுக்கு மட்டுமே உதவ முடியும். மற்ற கேள்விகளுக்கு 1800-XXX-XXXX என்ற எண்ணில் தொடர்பு கொள்ளவும்.",
            "telugu" to "KYC, లావాదేవీలు మరియు ఖాతా సంబంధిత ప్రశ్నలకు మాత్రమే సహాయం చేయగలను. ఇతర ప్రశ్నలకు 1800-XXX-XXXX కు కాల్ చేయండి.",
            "kannada" to "KYC, ವಹಿವಾಟುಗಳು ಮತ್ತು ಖಾತೆ ಸಂಬಂಧಿತ ಪ್ರಶ್ನೆಗಳಿಗೆ ಮಾತ್ರ ಸಹಾಯ ಮಾಡಬಹುದು. ಇತರ ಪ್ರಶ್ನೆಗಳಿಗೆ 1800-XXX-XXXX ಗೆ ಕರೆ ಮಾಡಿ.",
            "english" to "I can only help with KYC, transactions, and account-related queries. For other questions, please call our helpline at 1800-XXX-XXXX."
        )
    }

    fun create(
        userId: String,
        userMessage: String,
        intent: ExtractedIntent,
        conversationHistory: List<ConversationMessage>
    ): Escalation {
        val historyJson = objectMapper.writeValueAsString(conversationHistory)
        
        val escalation = Escalation(
            userId = userId,
            detectedLanguage = intent.detectedLanguage,
            originalQuery = userMessage,
            escalationReason = intent.escalationReason,
            conversationHistory = historyJson,
            status = "open"
        )
        
        val saved = escalationRepository.save(escalation)
        logger.info("Created escalation ${saved.id} for user $userId: ${intent.escalationReason}")
        
        return saved
    }

    fun getEscalationMessage(language: String, escalationId: Long): String {
        val template = ESCALATION_MESSAGES[language.lowercase()] ?: ESCALATION_MESSAGES["english"]!!
        return template.replace("{escalationId}", "ESC$escalationId")
    }

    fun getOutOfScopeMessage(language: String): String {
        return OUT_OF_SCOPE_MESSAGES[language.lowercase()] ?: OUT_OF_SCOPE_MESSAGES["english"]!!
    }

    fun getAllEscalations(): List<Escalation> {
        return escalationRepository.findAll()
    }

    fun getOpenEscalations(): List<Escalation> {
        return escalationRepository.findByStatus("open")
    }
}
