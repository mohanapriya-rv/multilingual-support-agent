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

    fun getDashboardStats(startDate: LocalDate? = null, endDate: LocalDate? = null): DashboardStats {
        val today = LocalDate.now()
        val start = startDate?.atStartOfDay() ?: today.atStartOfDay()
        val end = endDate?.plusDays(1)?.atStartOfDay() ?: today.plusDays(1).atStartOfDay()
        
        val dateRangeEvents = analyticsRepository.findByTimestampBetween(start, end)
        
        return DashboardStats(
            totalConversations = dateRangeEvents.map { it.sessionId }.distinct().size.toLong(),
            totalQueries = dateRangeEvents.size.toLong(),
            todayQueries = analyticsRepository.countByTimestampAfter(today.atStartOfDay()),
            kycQueries = dateRangeEvents.count { it.intentCategory == "kyc" },
            investmentQueries = dateRangeEvents.count { it.intentCategory == "mutual_fund" },
            transactionQueries = dateRangeEvents.count { it.intentCategory == "transaction" },
            escalations = dateRangeEvents.count { it.escalated },
            languageDistribution = getLanguageDistribution(dateRangeEvents),
            inputTypeDistribution = getInputTypeDistribution(dateRangeEvents),
            intentDistribution = getIntentDistribution(dateRangeEvents),
            successRate = calculateSuccessRate(dateRangeEvents),
            startDate = startDate ?: today,
            endDate = endDate ?: today
        )
    }

    fun getLanguageDistribution(events: List<AnalyticsEvent>? = null): Map<String, Long> {
        val eventList = events ?: analyticsRepository.findAll()
        return eventList
            .groupBy { it.language.lowercase() }
            .mapValues { it.value.size.toLong() }
    }

    fun getInputTypeDistribution(events: List<AnalyticsEvent>? = null): Map<String, Long> {
        val eventList = events ?: analyticsRepository.findAll()
        return eventList
            .groupBy { it.inputType }
            .mapValues { it.value.size.toLong() }
    }

    fun getIntentDistribution(events: List<AnalyticsEvent>? = null): Map<String, Long> {
        val eventList = events ?: analyticsRepository.findAll()
        return eventList
            .groupBy { it.intentCategory }
            .mapValues { it.value.size.toLong() }
    }

    fun calculateSuccessRate(events: List<AnalyticsEvent>? = null): Double {
        val eventList = events ?: analyticsRepository.findAll()
        val total = eventList.size
        if (total == 0) return 100.0
        val successful = eventList.count { it.responseGiven }
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
    val successRate: Double,
    val startDate: LocalDate,
    val endDate: LocalDate
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
