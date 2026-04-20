package com.support.agent.service

import com.support.agent.model.ExtractedIntent
import com.support.agent.repository.KycRepository
import com.support.agent.repository.TransactionRepository
import com.support.agent.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class DataFetcherService(
    private val userRepository: UserRepository,
    private val kycRepository: KycRepository,
    private val transactionRepository: TransactionRepository
) {
    private val logger = LoggerFactory.getLogger(DataFetcherService::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

    fun fetch(userId: String, intent: ExtractedIntent): Map<String, Any> {
        logger.info("Fetching data for userId=$userId, category=${intent.intentCategory}, type=${intent.intentType}")
        
        return when (intent.intentCategory.lowercase()) {
            "kyc" -> fetchKycData(userId, intent)
            "transaction" -> fetchTransactionData(userId, intent)
            "account" -> fetchAccountData(userId, intent)
            else -> emptyMap()
        }
    }

    private fun fetchKycData(userId: String, intent: ExtractedIntent): Map<String, Any> {
        val kyc = kycRepository.findByUserId(userId)
        val user = userRepository.findById(userId).orElse(null)
        
        if (kyc == null) {
            return mapOf("kyc_status" to "Not found", "message" to "No KYC record exists for this user")
        }

        return buildMap {
            put("kyc_status", kyc.status)
            put("documents_submitted", kyc.documentsSubmitted ?: "None")
            put("pending_documents", kyc.pendingDocuments ?: "None")
            if (kyc.rejectionReason != null) {
                put("rejection_reason", kyc.rejectionReason)
            }
            put("last_updated", kyc.lastUpdated.format(dateFormatter))
            user?.let { put("user_name", it.name) }
        }
    }

    private fun fetchTransactionData(userId: String, intent: ExtractedIntent): Map<String, Any> {
        val intentType = intent.intentType?.lowercase() ?: ""
        val entities = intent.entities
        
        return when {
            intentType.contains("specific") || entities.transactionId != null -> {
                val txnId = entities.transactionId ?: return mapOf("error" to "Transaction ID not provided")
                fetchSpecificTransaction(txnId)
            }
            intentType.contains("history") -> fetchTransactionHistory(userId)
            intentType.contains("failed") -> fetchFailedTransaction(userId)
            intentType.contains("refund") -> fetchRefundStatus(userId)
            entities.merchant != null -> fetchMerchantTransactions(userId, entities.merchant)
            else -> fetchRecentTransactions(userId)
        }
    }

    private fun fetchSpecificTransaction(transactionId: String): Map<String, Any> {
        val txn = transactionRepository.findById(transactionId).orElse(null)
            ?: return mapOf("error" to "Transaction not found")
        
        return buildMap {
            put("transaction_id", txn.id)
            put("amount", "₹${txn.amount}")
            put("merchant", txn.merchant ?: "N/A")
            put("status", txn.status)
            if (txn.failureReason != null) {
                put("failure_reason", txn.failureReason)
            }
            put("date", txn.createdAt.format(dateFormatter))
            if (txn.refundStatus != null) {
                put("refund_status", txn.refundStatus)
            }
            if (txn.refundDate != null) {
                put("refund_date", txn.refundDate.toString())
            }
        }
    }

    private fun fetchTransactionHistory(userId: String): Map<String, Any> {
        val transactions = transactionRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId)
        
        if (transactions.isEmpty()) {
            return mapOf("message" to "No transactions found")
        }

        val txnList = transactions.mapIndexed { index, txn ->
            "${index + 1}. ${txn.merchant ?: "Transfer"} - ₹${txn.amount} (${txn.status}) on ${txn.createdAt.format(dateFormatter)}"
        }
        
        return mapOf(
            "total_transactions" to transactions.size,
            "recent_transactions" to txnList.joinToString("\n")
        )
    }

    private fun fetchFailedTransaction(userId: String): Map<String, Any> {
        val txn = transactionRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(userId, "failed")
            ?: return mapOf("message" to "No failed transactions found")
        
        return buildMap {
            put("transaction_id", txn.id)
            put("amount", "₹${txn.amount}")
            put("merchant", txn.merchant ?: "N/A")
            put("status", txn.status)
            put("failure_reason", txn.failureReason ?: "Unknown reason")
            put("date", txn.createdAt.format(dateFormatter))
            if (txn.refundStatus != null) {
                put("refund_status", txn.refundStatus)
            }
        }
    }

    private fun fetchRefundStatus(userId: String): Map<String, Any> {
        val transactions = transactionRepository.findByUserId(userId)
            .filter { it.refundStatus != null }
        
        if (transactions.isEmpty()) {
            return mapOf("message" to "No refunds found for your account")
        }

        val refundInfo = transactions.map { txn ->
            "₹${txn.amount} - ${txn.merchant ?: "N/A"}: Refund ${txn.refundStatus}" +
                if (txn.refundDate != null) " (on ${txn.refundDate})" else ""
        }
        
        return mapOf(
            "refund_count" to transactions.size,
            "refund_details" to refundInfo.joinToString("\n")
        )
    }

    private fun fetchMerchantTransactions(userId: String, merchant: String): Map<String, Any> {
        val transactions = transactionRepository.findByUserIdAndMerchantContainingIgnoreCase(userId, merchant)
        
        if (transactions.isEmpty()) {
            return mapOf("message" to "No transactions found for $merchant")
        }

        val txnList = transactions.map { txn ->
            "${txn.merchant} - ₹${txn.amount} (${txn.status}) on ${txn.createdAt.format(dateFormatter)}" +
                if (txn.failureReason != null) " - Reason: ${txn.failureReason}" else ""
        }
        
        return mapOf(
            "merchant" to merchant,
            "transaction_count" to transactions.size,
            "transactions" to txnList.joinToString("\n")
        )
    }

    private fun fetchRecentTransactions(userId: String): Map<String, Any> {
        val oneWeekAgo = LocalDateTime.now().minusDays(7)
        val transactions = transactionRepository.findByUserIdAndCreatedAtAfter(userId, oneWeekAgo)
        
        if (transactions.isEmpty()) {
            return mapOf("message" to "No recent transactions in the last week")
        }

        val txnList = transactions.map { txn ->
            "${txn.merchant ?: "Transfer"} - ₹${txn.amount} (${txn.status})"
        }
        
        return mapOf(
            "period" to "Last 7 days",
            "transaction_count" to transactions.size,
            "transactions" to txnList.joinToString("\n")
        )
    }

    private fun fetchAccountData(userId: String, intent: ExtractedIntent): Map<String, Any> {
        val user = userRepository.findById(userId).orElse(null)
            ?: return mapOf("error" to "User not found")
        
        return buildMap {
            put("name", user.name)
            put("account_status", user.accountStatus)
            put("balance", "₹${user.balance}")
            
            if (user.accountStatus != "active" && user.blockReason != null) {
                put("block_reason", user.blockReason)
            }
            
            put("email", user.email ?: "Not set")
            put("phone", user.phone ?: "Not set")
            put("preferred_language", user.preferredLanguage)
            put("member_since", user.createdAt.format(dateFormatter))
        }
    }
}
