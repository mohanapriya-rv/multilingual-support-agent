package com.support.agent.controller

import com.support.agent.service.AnalyticsService
import com.support.agent.service.DashboardStats
import com.support.agent.service.DailyReport
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/analytics")
class AnalyticsController(
    private val analyticsService: AnalyticsService
) {
    @GetMapping("/dashboard")
    fun getDashboardStats(
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?
    ): ResponseEntity<DashboardStats> {
        val start = startDate?.let { LocalDate.parse(it) }
        val end = endDate?.let { LocalDate.parse(it) }
        return ResponseEntity.ok(analyticsService.getDashboardStats(start, end))
    }

    @GetMapping("/daily-report")
    fun getDailyReport(): ResponseEntity<DailyReport> {
        return ResponseEntity.ok(analyticsService.getDailyReport())
    }

    @GetMapping("/language-distribution")
    fun getLanguageDistribution(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(analyticsService.getLanguageDistribution())
    }

    @GetMapping("/intent-distribution")
    fun getIntentDistribution(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(analyticsService.getIntentDistribution())
    }
}
