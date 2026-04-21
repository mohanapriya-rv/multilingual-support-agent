package com.support.agent.service

import com.support.agent.model.ExtractedIntent
import com.support.agent.repository.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class DataFetcherService(
    private val userRepository: UserRepository,
    private val kycRepository: KycRepository,
    private val transactionRepository: TransactionRepository,
    private val mutualFundRepository: MutualFundRepository,
    private val userInvestmentRepository: UserInvestmentRepository,
    private val sipRepository: SipRepository
) {
    private val logger = LoggerFactory.getLogger(DataFetcherService::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    private val dateOnlyFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    fun fetch(userId: String, intent: ExtractedIntent): Map<String, Any> {
        logger.info("Fetching data for userId=$userId, category=${intent.intentCategory}, type=${intent.intentType}")
        
        return when (intent.intentCategory.lowercase()) {
            "kyc" -> fetchKycData(userId, intent)
            "transaction" -> fetchTransactionData(userId, intent)
            "account" -> fetchAccountData(userId, intent)
            "mutual_fund" -> fetchMutualFundData(userId, intent)
            "tax" -> fetchTaxData(userId, intent)
            else -> emptyMap()
        }
    }
    
    private fun fetchMutualFundData(userId: String, intent: ExtractedIntent): Map<String, Any> {
        val intentType = intent.intentType?.lowercase() ?: ""
        
        return when {
            intentType.contains("portfolio") -> fetchPortfolioValue(userId)
            intentType.contains("sip_status") || intentType.contains("sip") && intentType.contains("status") -> fetchSipStatus(userId)
            intentType.contains("sip") -> fetchSipDetails(userId)
            intentType.contains("nav") -> fetchFundNav(intent.entities.fundName)
            intentType.contains("return") -> fetchFundReturns(userId)
            intentType.contains("recommend") -> fetchFundRecommendations()
            intentType.contains("redemption") || intentType.contains("withdraw") -> fetchRedemptionInfo(userId)
            else -> fetchPortfolioValue(userId)
        }
    }
    
    private fun fetchPortfolioValue(userId: String): Map<String, Any> {
        val investments = userInvestmentRepository.findByUserId(userId)
        val totalValue = userInvestmentRepository.getTotalPortfolioValue(userId) ?: java.math.BigDecimal.ZERO
        val totalInvested = userInvestmentRepository.getTotalInvestedAmount(userId) ?: java.math.BigDecimal.ZERO
        val returns = totalValue.subtract(totalInvested)
        val returnsPercent = if (totalInvested > java.math.BigDecimal.ZERO) 
            returns.multiply(java.math.BigDecimal(100)).divide(totalInvested, 2, java.math.RoundingMode.HALF_UP)
        else java.math.BigDecimal.ZERO
        
        if (investments.isEmpty()) {
            return mapOf("message" to "No investments found. Start investing today!")
        }
        
        val investmentList = investments.map { inv ->
            "${inv.fundName}: ₹${inv.currentValue} (Invested: ₹${inv.investedAmount})"
        }
        
        return mapOf(
            "total_portfolio_value" to "₹${totalValue}",
            "total_invested" to "₹${totalInvested}",
            "total_returns" to "₹${returns} (${returnsPercent}%)",
            "number_of_funds" to investments.size,
            "investments" to investmentList.joinToString("\n")
        )
    }
    
    private fun fetchSipStatus(userId: String): Map<String, Any> {
        val activeSips = sipRepository.findByUserIdAndStatus(userId, "active")
        val allSips = sipRepository.findByUserId(userId)
        
        if (allSips.isEmpty()) {
            return mapOf("message" to "No SIPs found. Start a SIP today with as low as ₹500!")
        }
        
        val sipList = allSips.map { sip ->
            "${sip.fundName}: ₹${sip.amount}/${sip.frequency} - Status: ${sip.status.uppercase()}, Next: ${sip.nextDate.format(dateOnlyFormatter)}"
        }
        
        return mapOf(
            "active_sips" to activeSips.size,
            "total_sips" to allSips.size,
            "monthly_sip_amount" to "₹${activeSips.sumOf { it.amount }}",
            "sip_details" to sipList.joinToString("\n")
        )
    }
    
    private fun fetchSipDetails(userId: String): Map<String, Any> {
        val sips = sipRepository.findByUserId(userId)
        
        if (sips.isEmpty()) {
            return mapOf("message" to "No SIPs found")
        }
        
        val sipList = sips.map { sip ->
            "SIP ID: ${sip.id}\nFund: ${sip.fundName}\nAmount: ₹${sip.amount}\nStatus: ${sip.status}\nNext Date: ${sip.nextDate}\nInstallments: ${sip.completedInstallments}/${sip.totalInstallments}"
        }
        
        return mapOf(
            "sip_count" to sips.size,
            "sip_details" to sipList.joinToString("\n\n")
        )
    }
    
    private fun fetchFundNav(fundName: String?): Map<String, Any> {
        val funds = if (fundName != null) {
            mutualFundRepository.findAll().filter { it.name.contains(fundName, ignoreCase = true) }
        } else {
            mutualFundRepository.findAll().take(5)
        }
        
        if (funds.isEmpty()) {
            return mapOf("message" to "Fund not found. Please check the fund name.")
        }
        
        val fundList = funds.map { fund ->
            "${fund.name}: NAV ₹${fund.nav} (1Y: ${fund.oneYearReturn}%)"
        }
        
        return mapOf(
            "nav_details" to fundList.joinToString("\n")
        )
    }
    
    private fun fetchFundReturns(userId: String): Map<String, Any> {
        val investments = userInvestmentRepository.findByUserId(userId)
        
        if (investments.isEmpty()) {
            return mapOf("message" to "No investments found")
        }
        
        val returnsList = investments.map { inv ->
            val returnAmount = inv.currentValue.subtract(inv.investedAmount)
            val returnPercent = returnAmount.multiply(java.math.BigDecimal(100))
                .divide(inv.investedAmount, 2, java.math.RoundingMode.HALF_UP)
            "${inv.fundName}: ${if (returnAmount >= java.math.BigDecimal.ZERO) "+" else ""}₹${returnAmount} (${returnPercent}%)"
        }
        
        return mapOf(
            "returns_summary" to returnsList.joinToString("\n")
        )
    }
    
    private fun fetchFundRecommendations(): Map<String, Any> {
        val topFunds = mutualFundRepository.findTopPerformers().take(5)
        
        val recommendations = topFunds.map { fund ->
            "${fund.name} (${fund.category})\n  • 1Y Return: ${fund.oneYearReturn}%\n  • Risk: ${fund.riskLevel}\n  • Min SIP: ₹${fund.minSipAmount}"
        }
        
        return mapOf(
            "top_funds" to recommendations.joinToString("\n\n"),
            "disclaimer" to "Past performance doesn't guarantee future returns. Please read scheme documents."
        )
    }
    
    private fun fetchRedemptionInfo(userId: String): Map<String, Any> {
        val investments = userInvestmentRepository.findByUserId(userId)
        val totalValue = userInvestmentRepository.getTotalPortfolioValue(userId) ?: java.math.BigDecimal.ZERO
        
        if (investments.isEmpty()) {
            return mapOf("message" to "No investments available for redemption")
        }
        
        return mapOf(
            "redeemable_amount" to "₹${totalValue}",
            "funds_count" to investments.size,
            "note" to "Redemption typically takes 2-3 business days. Exit load may apply for investments < 1 year."
        )
    }
    
    private fun fetchTaxData(userId: String, intent: ExtractedIntent): Map<String, Any> {
        val investments = userInvestmentRepository.findByUserId(userId)
        val totalInvested = userInvestmentRepository.getTotalInvestedAmount(userId) ?: java.math.BigDecimal.ZERO
        val totalValue = userInvestmentRepository.getTotalPortfolioValue(userId) ?: java.math.BigDecimal.ZERO
        val capitalGains = totalValue.subtract(totalInvested)
        
        return mapOf(
            "total_invested" to "₹${totalInvested}",
            "current_value" to "₹${totalValue}",
            "unrealized_gains" to "₹${capitalGains}",
            "tax_note" to "LTCG (>1 year) taxed at 10% above ₹1 lakh. STCG (<1 year) taxed at 15%.",
            "statement_available" to "Capital Gains statement can be downloaded from the app."
        )
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
            try {
                put("last_updated", kyc.lastUpdated.format(dateFormatter))
            } catch (e: Exception) {
                logger.error("Error formatting last_updated date", e)
                put("last_updated", kyc.lastUpdated.toString())
            }
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
