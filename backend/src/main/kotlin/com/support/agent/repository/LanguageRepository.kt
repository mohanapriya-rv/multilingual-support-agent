package com.support.agent.repository

import com.support.agent.entity.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LanguageRepository : JpaRepository<Language, String> {
    
    fun findByIsActiveTrue(): List<Language>
    
    fun findByCodeIgnoreCase(code: String): Language?
    
    fun findByNameIgnoreCase(name: String): Language?
    
    @Query("SELECT l FROM Language l WHERE l.isActive = true ORDER BY l.displayOrder")
    fun findAllActiveOrdered(): List<Language>
    
    @Query("SELECT l.code FROM Language l WHERE l.isActive = true")
    fun findAllActiveCodes(): List<String>
}
