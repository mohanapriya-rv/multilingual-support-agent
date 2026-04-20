package com.support.agent.controller

import com.support.agent.model.ChatRequest
import com.support.agent.model.ChatResponse
import com.support.agent.service.AgentService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val agentService: AgentService
) {
    @PostMapping
    fun chat(@Valid @RequestBody request: ChatRequest): ResponseEntity<ChatResponse> {
        val response = agentService.processMessage(request)
        return ResponseEntity.ok(response)
    }
}
