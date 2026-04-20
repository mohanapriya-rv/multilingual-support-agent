package com.support.agent.controller

import com.support.agent.service.AnalyticsService
import com.support.agent.service.DashboardStats
import com.support.agent.service.DailyReport
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/analytics")
class AnalyticsController(
    private val analyticsService: AnalyticsService
) {
    @GetMapping("/dashboard")
    fun getDashboardStats(): ResponseEntity<DashboardStats> {
        return ResponseEntity.ok(analyticsService.getDashboardStats())
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
