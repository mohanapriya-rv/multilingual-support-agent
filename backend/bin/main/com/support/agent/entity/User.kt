package com.support.agent.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(length = 20)
    val id: String,
    
    @Column(length = 100, nullable = false)
    val name: String,
    
    @Column(length = 100)
    val email: String? = null,
    
    @Column(length = 15)
    val phone: String? = null,
    
    @Column(name = "account_status", length = 30)
    val accountStatus: String = "active",
    
    @Column(name = "block_reason", columnDefinition = "TEXT")
    val blockReason: String? = null,
    
    @Column(precision = 15, scale = 2)
    val balance: BigDecimal = BigDecimal.ZERO,
    
    @Column(name = "preferred_language", length = 20)
    val preferredLanguage: String = "english",
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
