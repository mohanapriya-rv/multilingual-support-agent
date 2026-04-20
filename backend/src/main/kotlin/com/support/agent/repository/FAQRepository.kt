package com.support.agent.repository

import com.support.agent.entity.FAQ
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FAQRepository : JpaRepository<FAQ, Long> {
    
    fun findByCategory(category: String): List<FAQ>
    
    @Query("SELECT f FROM FAQ f WHERE f.category = :category ORDER BY f.priority DESC")
    fun findByCategoryOrderByPriority(category: String): List<FAQ>
    
    @Query("SELECT f FROM FAQ f WHERE LOWER(f.keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    fun findByKeywordContaining(keyword: String): List<FAQ>
    
    @Query("""
        SELECT f FROM FAQ f 
        WHERE f.category = :category 
        OR LOWER(f.keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY f.priority DESC
    """)
    fun findRelevantFAQs(category: String, keyword: String): List<FAQ>
}
