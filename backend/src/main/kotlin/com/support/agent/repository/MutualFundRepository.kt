package com.support.agent.repository

import com.support.agent.entity.MutualFund
import com.support.agent.entity.SipRecord
import com.support.agent.entity.UserInvestment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MutualFundRepository : JpaRepository<MutualFund, String> {
    fun findByCategory(category: String): List<MutualFund>
    
    @Query("SELECT f FROM MutualFund f ORDER BY f.oneYearReturn DESC")
    fun findTopPerformers(): List<MutualFund>
}

interface UserInvestmentRepository : JpaRepository<UserInvestment, Long> {
    fun findByUserId(userId: String): List<UserInvestment>
    
    @Query("SELECT SUM(i.currentValue) FROM UserInvestment i WHERE i.userId = :userId")
    fun getTotalPortfolioValue(userId: String): java.math.BigDecimal?
    
    @Query("SELECT SUM(i.investedAmount) FROM UserInvestment i WHERE i.userId = :userId")
    fun getTotalInvestedAmount(userId: String): java.math.BigDecimal?
}

interface SipRepository : JpaRepository<SipRecord, String> {
    fun findByUserId(userId: String): List<SipRecord>
    fun findByUserIdAndStatus(userId: String, status: String): List<SipRecord>
}
