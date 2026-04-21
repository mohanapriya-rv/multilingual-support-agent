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
    private val objectMapper: ObjectMapper,
    private val languageService: LanguageService  // NEW: Inject language service
) {
    private val logger = LoggerFactory.getLogger(EscalationService::class.java)
    // REMOVED: Hardcoded ESCALATION_MESSAGES and OUT_OF_SCOPE_MESSAGES
    // Now loaded dynamically from database via LanguageService

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
        // Now uses dynamic messages from database
        return languageService.getEscalationMessage(language, "ESC$escalationId")
    }

    fun getOutOfScopeMessage(language: String): String {
        // Now uses dynamic messages from database
        return languageService.getOutOfScopeMessage(language)
    }

    fun getAllEscalations(): List<Escalation> {
        return escalationRepository.findAll()
    }

    fun getOpenEscalations(): List<Escalation> {
        return escalationRepository.findByStatus("open")
    }
}
