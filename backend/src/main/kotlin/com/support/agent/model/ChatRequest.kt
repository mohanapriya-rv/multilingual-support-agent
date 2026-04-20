package com.support.agent.model

import jakarta.validation.constraints.NotBlank

data class ChatRequest(
    @field:NotBlank(message = "userId is required")
    val userId: String,
    
    @field:NotBlank(message = "message is required")
    val message: String,
    
    val sessionId: String? = null,
    
    val conversationHistory: List<ConversationMessage> = emptyList(),
    
    val inputType: String? = "text"  // "text" or "voice"
)
