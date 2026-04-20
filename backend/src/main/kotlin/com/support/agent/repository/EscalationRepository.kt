package com.support.agent.repository

import com.support.agent.entity.Escalation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EscalationRepository : JpaRepository<Escalation, Long> {
    fun findByStatus(status: String): List<Escalation>
    fun findByUserId(userId: String): List<Escalation>
}
