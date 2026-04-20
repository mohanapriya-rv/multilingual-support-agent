package com.support.agent.controller

import com.support.agent.entity.Escalation
import com.support.agent.service.EscalationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/escalations")
class EscalationController(
    private val escalationService: EscalationService
) {
    @GetMapping
    fun getAllEscalations(): ResponseEntity<List<Escalation>> {
        return ResponseEntity.ok(escalationService.getAllEscalations())
    }

    @GetMapping("/open")
    fun getOpenEscalations(): ResponseEntity<List<Escalation>> {
        return ResponseEntity.ok(escalationService.getOpenEscalations())
    }
}
