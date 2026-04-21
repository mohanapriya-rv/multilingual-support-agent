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
            val saved = analyticsRepository.save(event)
            logger.info("Analytics event tracked: id=${saved.id}, user=$userId, session=$sessionId, intent=$intentCategory, language=$language")
        } catch (e: Exception) {
            logger.error("ANALYTICS INSERT FAILED for user=$userId, session=$sessionId, intent=$intentCategory: ${e.message}", e)
        }
    }

    fun verifyAnalyticsTable(): Map<String, Any> {
        return try {
            val count = analyticsRepository.count()
            val result = mapOf<String, Any>("tableExists" to true, "totalRecords" to count)
            logger.info("Analytics table verification: $result")
            result
        } catch (e: Exception) {
            logger.error("Analytics table verification FAILED: ${e.message}", e)
            mapOf("tableExists" to false, "error" to (e.message ?: "unknown"))
        }
    }

    fun getDashboardStats(startDate: LocalDate? = null, endDate: LocalDate? = null): DashboardStats {
        val today = LocalDate.now()
        val start = startDate?.atStartOfDay() ?: today.minusDays(7).atStartOfDay()
        val end = endDate?.plusDays(1)?.atStartOfDay() ?: today.plusDays(1).atStartOfDay()

        val dateRangeEvents = analyticsRepository.findByTimestampBetween(start, end)
        val escalatedEvents = dateRangeEvents.filter { it.escalated }
        val allEvents = analyticsRepository.findAll()

        return DashboardStats(
            totalConversations = dateRangeEvents.map { it.sessionId }.distinct().size.toLong(),
            totalQueries = dateRangeEvents.size.toLong(),
            allTimeQueries = allEvents.size.toLong(),
            allTimeConversations = allEvents.map { it.sessionId }.distinct().size.toLong(),
            todayQueries = analyticsRepository.countByTimestampAfter(today.atStartOfDay()),
            kycQueries = dateRangeEvents.count { it.intentCategory == "kyc" }.toLong(),
            investmentQueries = dateRangeEvents.count { it.intentCategory == "mutual_fund" }.toLong(),
            transactionQueries = dateRangeEvents.count { it.intentCategory == "transaction" }.toLong(),
            escalations = escalatedEvents.size.toLong(),
            escalationRate = calculateEscalationRate(dateRangeEvents),
            escalationByIntent = getEscalationByIntent(escalatedEvents),
            escalationByLanguage = getEscalationByLanguage(escalatedEvents),
            languageDistribution = getLanguageDistribution(dateRangeEvents),
            inputTypeDistribution = getInputTypeDistribution(dateRangeEvents),
            intentDistribution = getIntentDistribution(dateRangeEvents),
            successRate = calculateSuccessRate(dateRangeEvents),
            startDate = startDate ?: today.minusDays(7),
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

    fun calculateEscalationRate(events: List<AnalyticsEvent>? = null): Double {
        val eventList = events ?: analyticsRepository.findAll()
        val total = eventList.size
        if (total == 0) return 0.0
        val escalated = eventList.count { it.escalated }
        return (escalated.toDouble() / total.toDouble()) * 100
    }

    fun getEscalationByIntent(events: List<AnalyticsEvent>): Map<String, Long> {
        return events
            .groupBy { it.intentCategory }
            .mapValues { it.value.size.toLong() }
    }

    fun getEscalationByLanguage(events: List<AnalyticsEvent>): Map<String, Long> {
        return events
            .groupBy { it.language }
            .mapValues { it.value.size.toLong() }
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
    val allTimeQueries: Long,
    val allTimeConversations: Long,
    val todayQueries: Long,
    val kycQueries: Long,
    val investmentQueries: Long,
    val transactionQueries: Long,
    val escalations: Long,
    val escalationRate: Double,
    val escalationByIntent: Map<String, Long>,
    val escalationByLanguage: Map<String, Long>,
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
