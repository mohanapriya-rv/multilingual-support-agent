package com.support.agent.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "analytics_events")
data class AnalyticsEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "user_id", length = 50)
    val userId: String,
    
    @Column(name = "session_id", length = 100)
    val sessionId: String,
    
    @Column(length = 30)
    val language: String,
    
    @Column(name = "input_type", length = 20)
    val inputType: String,
    
    @Column(name = "intent_category", length = 50)
    val intentCategory: String,
    
    @Column(name = "intent_type", length = 50)
    val intentType: String,
    
    @Column(name = "response_given")
    val responseGiven: Boolean,
    
    val escalated: Boolean,
    
    val confidence: Double,
    
    val timestamp: LocalDateTime = LocalDateTime.now()
)
