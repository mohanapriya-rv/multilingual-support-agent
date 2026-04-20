package com.support.agent.repository

import com.support.agent.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TransactionRepository : JpaRepository<Transaction, String> {
    fun findByUserId(userId: String): List<Transaction>
    
    fun findTop10ByUserIdOrderByCreatedAtDesc(userId: String): List<Transaction>
    
    fun findFirstByUserIdAndStatusOrderByCreatedAtDesc(userId: String, status: String): Transaction?
    
    fun findByUserIdAndMerchantContainingIgnoreCase(userId: String, merchant: String): List<Transaction>
    
    fun findByUserIdAndCreatedAtAfter(userId: String, after: LocalDateTime): List<Transaction>
}
