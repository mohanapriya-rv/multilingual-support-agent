package com.support.agent.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @Column(length = 20)
    val id: String,
    
    @Column(name = "user_id", length = 20, nullable = false)
    val userId: String,
    
    @Column(precision = 15, scale = 2, nullable = false)
    val amount: BigDecimal,
    
    @Column(length = 20)
    val status: String = "pending",
    
    @Column(length = 100)
    val merchant: String? = null,
    
    @Column(name = "failure_reason", columnDefinition = "TEXT")
    val failureReason: String? = null,
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "refund_status", length = 20)
    val refundStatus: String? = null,
    
    @Column(name = "refund_date")
    val refundDate: LocalDate? = null
)
