package com.support.agent.entity

import jakarta.persistence.*

@Entity
@Table(name = "faqs")
data class FAQ(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val category: String,  // "mutual_fund", "sip", "kyc", "account", etc.
    
    @Column(nullable = false, length = 500)
    val question: String,
    
    @Column(nullable = false, length = 2000)
    val answer: String,
    
    @Column(name = "question_hindi", length = 500)
    val questionHindi: String? = null,
    
    @Column(name = "answer_hindi", length = 2000)
    val answerHindi: String? = null,
    
    @Column(name = "question_tamil", length = 500)
    val questionTamil: String? = null,
    
    @Column(name = "answer_tamil", length = 2000)
    val answerTamil: String? = null,
    
    @Column(nullable = false)
    val keywords: String,  // comma-separated: "sip,start,begin,new"
    
    @Column(nullable = false)
    val priority: Int = 0  // Higher = more relevant
)
