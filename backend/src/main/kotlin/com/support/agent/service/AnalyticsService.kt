package com.support.agent.service

import com.support.agent.model.AnalyticsEvent
import com.support.agent.repository.AnalyticsRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class AnalyticsService(
    private val analyticsRepository: AnalyticsRepository
) {
    private val logger = LoggerFactory.getLogger(AnalyticsService::class.java)

    fun trackInteraction(
        userId: String,
        sessionId: String,
        language: String,
        inputType: String,
        intentCategory: String?,
        intentType: String?,
        responseGiven: Boolean,
        escalated: Boolean,
        confidence: Double
    ) {
        val event = AnalyticsEvent(
            userId = userId,
            sessionId = sessionId,
            language = language,
            inputType = inputType,
            intentCategory = intentCategory ?: "unknown",
            intentType = intentType ?: "unknown",
            responseGiven = responseGiven,
            escalated = escalated,
            confidence = confidence,
            timestamp = LocalDateTime.now()
        )
        
        try {
            analyticsRepository.save(event)
            logger.debug("Analytics event tracked for user: $userId, intent: $intentCategory")
        } catch (e: Exception) {
            logger.error("Failed to track analytics event: ${e.message}")
        }
    }

    fun getDashboardStats(): DashboardStats {
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay()
        
        return DashboardStats(
            totalConversations = analyticsRepository.countDistinctSessions(),
            totalQueries = analyticsRepository.count(),
            todayQueries = analyticsRepository.countByTimestampAfter(startOfDay),
            kycQueries = analyticsRepository.countByIntentCategory("kyc"),
            investmentQueries = analyticsRepository.countByIntentCategory("mutual_fund"),
            transactionQueries = analyticsRepository.countByIntentCategory("transaction"),
            escalations = analyticsRepository.countByEscalated(true),
            languageDistribution = getLanguageDistribution(),
            inputTypeDistribution = getInputTypeDistribution(),
            intentDistribution = getIntentDistribution(),
            successRate = calculateSuccessRate()
        )
    }

    fun getLanguageDistribution(): Map<String, Long> {
        return analyticsRepository.findAll()
            .groupBy { it.language.lowercase() }
            .mapValues { it.value.size.toLong() }
    }

    fun getInputTypeDistribution(): Map<String, Long> {
        return analyticsRepository.findAll()
            .groupBy { it.inputType }
            .mapValues { it.value.size.toLong() }
    }

    fun getIntentDistribution(): Map<String, Long> {
        return analyticsRepository.findAll()
            .groupBy { it.intentCategory }
            .mapValues { it.value.size.toLong() }
    }

    fun calculateSuccessRate(): Double {
        val total = analyticsRepository.count()
        if (total == 0L) return 100.0
        val successful = analyticsRepository.countByResponseGiven(true)
        return (successful.toDouble() / total.toDouble()) * 100
    }

    fun getDailyReport(): DailyReport {
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay()
        val endOfDay = today.plusDays(1).atStartOfDay()
        
        val todayEvents = analyticsRepository.findByTimestampBetween(startOfDay, endOfDay)
        
        return DailyReport(
            date = today,
            totalUsers = todayEvents.map { it.userId }.distinct().size,
            totalQueries = todayEvents.size,
            kycQueries = todayEvents.count { it.intentCategory == "kyc" },
            investmentQueries = todayEvents.count { it.intentCategory == "mutual_fund" },
            transactionQueries = todayEvents.count { it.intentCategory == "transaction" },
            escalations = todayEvents.count { it.escalated },
            languageBreakdown = todayEvents.groupBy { it.language }.mapValues { it.value.size },
            topIssues = getTopIssues(todayEvents)
        )
    }

    private fun getTopIssues(events: List<AnalyticsEvent>): List<String> {
        return events
            .groupBy { it.intentType }
            .entries
            .sortedByDescending { it.value.size }
            .take(5)
            .map { "${it.key} (${it.value.size})" }
    }
}

data class DashboardStats(
    val totalConversations: Long,
    val totalQueries: Long,
    val todayQueries: Long,
    val kycQueries: Long,
    val investmentQueries: Long,
    val transactionQueries: Long,
    val escalations: Long,
    val languageDistribution: Map<String, Long>,
    val inputTypeDistribution: Map<String, Long>,
    val intentDistribution: Map<String, Long>,
    val successRate: Double
)

data class DailyReport(
    val date: LocalDate,
    val totalUsers: Int,
    val totalQueries: Int,
    val kycQueries: Int,
    val investmentQueries: Int,
    val transactionQueries: Int,
    val escalations: Int,
    val languageBreakdown: Map<String, Int>,
    val topIssues: List<String>
)
