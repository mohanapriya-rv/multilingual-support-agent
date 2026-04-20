package com.support.agent.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "kyc_records")
data class KycRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "user_id", length = 20, nullable = false)
    val userId: String,
    
    @Column(length = 20)
    val status: String = "not_submitted",
    
    @Column(name = "documents_submitted", columnDefinition = "TEXT")
    val documentsSubmitted: String? = null,
    
    @Column(name = "pending_documents", columnDefinition = "TEXT")
    val pendingDocuments: String? = null,
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    val rejectionReason: String? = null,
    
    @Column(name = "last_updated")
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)
