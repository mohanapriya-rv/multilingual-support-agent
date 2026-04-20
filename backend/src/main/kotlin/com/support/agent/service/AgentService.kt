package com.support.agent.service

import com.support.agent.model.ChatRequest
import com.support.agent.model.ChatResponse
import com.support.agent.model.ConversationMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AgentService(
    private val intentExtractorService: IntentExtractorService,
    private val dataFetcherService: DataFetcherService,
    private val responseFormatterService: ResponseFormatterService,
    private val escalationService: EscalationService
) {
    private val logger = LoggerFactory.getLogger(AgentService::class.java)

    fun processMessage(request: ChatRequest): ChatResponse {
        val sessionId = request.sessionId ?: UUID.randomUUID().toString()
        
        logger.info("Processing message from user ${request.userId}: ${request.message}")

        // Step 1: Extract intent using Claude
        val extractedIntent = intentExtractorService.extract(
            request.message,
            request.conversationHistory
        )
        
        logger.info("Extracted intent: category=${extractedIntent.intentCategory}, type=${extractedIntent.intentType}, language=${extractedIntent.detectedLanguage}")

        // Step 2: Check for escalation
        if (extractedIntent.requiresEscalation) {
            val escalation = escalationService.create(
                request.userId,
                request.message,
                extractedIntent,
                request.conversationHistory
            )
            
            val escalationMessage = escalationService.getEscalationMessage(
                extractedIntent.detectedLanguage,
                escalation.id
            )
            
            return ChatResponse(
                sessionId = sessionId,
                response = escalationMessage,
                detectedLanguage = extractedIntent.detectedLanguage,
                intentCategory = extractedIntent.intentCategory,
                intentType = extractedIntent.intentType,
                escalated = true,
                escalationId = escalation.id,
                confidence = extractedIntent.confidence
            )
        }

        // Step 3: Check if query is in scope
        if (!extractedIntent.isInScope) {
            val outOfScopeMessage = escalationService.getOutOfScopeMessage(extractedIntent.detectedLanguage)
            
            return ChatResponse(
                sessionId = sessionId,
                response = outOfScopeMessage,
                detectedLanguage = extractedIntent.detectedLanguage,
                intentCategory = extractedIntent.intentCategory,
                intentType = extractedIntent.intentType,
                escalated = false,
                confidence = extractedIntent.confidence
            )
        }

        // Step 4: Fetch data from database
        val data = dataFetcherService.fetch(request.userId, extractedIntent)
        
        logger.info("Fetched data: $data")

        // Step 5: Format response using Gemini (with FAQ context)
        val response = responseFormatterService.format(
            request.message,
            extractedIntent.detectedLanguage,
            extractedIntent.intentCategory,  // NEW: Pass intent category for FAQ lookup
            data,
            request.conversationHistory
        )

        return ChatResponse(
            sessionId = sessionId,
            response = response,
            detectedLanguage = extractedIntent.detectedLanguage,
            intentCategory = extractedIntent.intentCategory,
            intentType = extractedIntent.intentType,
            escalated = false,
            confidence = extractedIntent.confidence
        )
    }
}
