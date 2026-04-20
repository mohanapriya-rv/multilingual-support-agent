package com.support.agent.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "escalations")
data class Escalation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "user_id", length = 20)
    val userId: String,
    
    @Column(name = "detected_language", length = 20)
    val detectedLanguage: String,
    
    @Column(name = "original_query", columnDefinition = "TEXT", nullable = false)
    val originalQuery: String,
    
    @Column(name = "escalation_reason", columnDefinition = "TEXT")
    val escalationReason: String? = null,
    
    @Column(name = "conversation_history", columnDefinition = "TEXT")
    val conversationHistory: String? = null,
    
    @Column(name = "assigned_agent", length = 100)
    val assignedAgent: String? = null,
    
    @Column(length = 20)
    val status: String = "open",
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
