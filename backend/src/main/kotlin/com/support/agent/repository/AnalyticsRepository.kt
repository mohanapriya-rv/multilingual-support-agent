package com.support.agent.repository

import com.support.agent.model.AnalyticsEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AnalyticsRepository : JpaRepository<AnalyticsEvent, Long> {
    
    fun countByIntentCategory(intentCategory: String): Long
    
    fun countByEscalated(escalated: Boolean): Long
    
    fun countByResponseGiven(responseGiven: Boolean): Long
    
    fun countByTimestampAfter(timestamp: LocalDateTime): Long
    
    fun findByTimestampBetween(start: LocalDateTime, end: LocalDateTime): List<AnalyticsEvent>
    
    fun findByUserId(userId: String): List<AnalyticsEvent>
    
    @Query("SELECT COUNT(DISTINCT e.sessionId) FROM AnalyticsEvent e")
    fun countDistinctSessions(): Long
    
    @Query("SELECT e.language, COUNT(e) FROM AnalyticsEvent e GROUP BY e.language")
    fun getLanguageStats(): List<Array<Any>>
    
    @Query("SELECT e.intentCategory, COUNT(e) FROM AnalyticsEvent e GROUP BY e.intentCategory")
    fun getIntentStats(): List<Array<Any>>
}
