package com.support.agent.repository

import com.support.agent.entity.KycRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KycRepository : JpaRepository<KycRecord, Long> {
    fun findByUserId(userId: String): KycRecord?
}
