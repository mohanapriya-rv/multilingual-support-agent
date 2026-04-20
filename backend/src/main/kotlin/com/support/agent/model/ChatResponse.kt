package com.support.agent.model

data class ChatResponse(
    val sessionId: String,
    val response: String,
    val detectedLanguage: String,
    val intentCategory: String?,
    val intentType: String?,
    val escalated: Boolean = false,
    val escalationId: Long? = null,
    val confidence: Double = 0.0
)
