package com.support.agent.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "mutual_funds")
data class MutualFund(
    @Id
    val id: String,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false)
    val category: String, // equity, debt, hybrid, elss
    
    @Column(precision = 10, scale = 4)
    val nav: BigDecimal,
    
    @Column(name = "one_year_return", precision = 5, scale = 2)
    val oneYearReturn: BigDecimal?,
    
    @Column(name = "three_year_return", precision = 5, scale = 2)
    val threeYearReturn: BigDecimal?,
    
    @Column(name = "risk_level")
    val riskLevel: String, // low, moderate, high
    
    @Column(name = "min_sip_amount", precision = 10, scale = 2)
    val minSipAmount: BigDecimal = BigDecimal("500.00")
)

@Entity
@Table(name = "user_investments")
data class UserInvestment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "user_id", nullable = false)
    val userId: String,
    
    @Column(name = "fund_id", nullable = false)
    val fundId: String,
    
    @Column(name = "fund_name")
    val fundName: String,
    
    @Column(name = "invested_amount", precision = 15, scale = 2)
    val investedAmount: BigDecimal,
    
    @Column(name = "current_value", precision = 15, scale = 2)
    val currentValue: BigDecimal,
    
    @Column(precision = 10, scale = 4)
    val units: BigDecimal,
    
    @Column(name = "purchase_date")
    val purchaseDate: LocalDate
)

@Entity
@Table(name = "sip_records")
data class SipRecord(
    @Id
    val id: String,
    
    @Column(name = "user_id", nullable = false)
    val userId: String,
    
    @Column(name = "fund_id", nullable = false)
    val fundId: String,
    
    @Column(name = "fund_name")
    val fundName: String,
    
    @Column(precision = 10, scale = 2)
    val amount: BigDecimal,
    
    @Column(nullable = false)
    val frequency: String = "monthly", // monthly, weekly, quarterly
    
    @Column(name = "sip_date")
    val sipDate: Int = 1, // Day of month
    
    @Column(nullable = false)
    val status: String = "active", // active, paused, stopped
    
    @Column(name = "start_date")
    val startDate: LocalDate,
    
    @Column(name = "next_date")
    val nextDate: LocalDate,
    
    @Column(name = "total_installments")
    val totalInstallments: Int = 0,
    
    @Column(name = "completed_installments")
    val completedInstallments: Int = 0
)
