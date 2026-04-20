package com.support.agent.entity

import jakarta.persistence.*

@Entity
@Table(name = "languages")
data class Language(
    @Id
    val code: String,  // "hi", "ta", "te", "kn", "en", "ml", "bn", "mr", "gu", "pa"
    
    @Column(nullable = false)
    val name: String,  // "Hindi", "Tamil", etc.
    
    @Column(name = "native_name", nullable = false)
    val nativeName: String,  // "हिंदी", "தமிழ்", etc.
    
    @Column(nullable = false)
    val greeting: String,  // "नमस्ते", "வணக்கம்", etc.
    
    @Column(name = "fallback_message", length = 500)
    val fallbackMessage: String,  // Fallback response in this language
    
    @Column(name = "escalation_message", length = 500)
    val escalationMessage: String,  // Escalation message template
    
    @Column(name = "out_of_scope_message", length = 500)
    val outOfScopeMessage: String,  // Out of scope message
    
    @Column(name = "flag_emoji")
    val flagEmoji: String = "🇮🇳",
    
    @Column(name = "is_active")
    val isActive: Boolean = true,
    
    @Column(name = "display_order")
    val displayOrder: Int = 0
)
