package com.support.agent.service

import com.support.agent.entity.Language
import com.support.agent.repository.LanguageRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class LanguageService(
    private val languageRepository: LanguageRepository
) {
    private val logger = LoggerFactory.getLogger(LanguageService::class.java)
    
    // In-memory cache for quick access (lazy-loaded)
    private var languageCache: Map<String, Language>? = null
    private var defaultLanguage: Language? = null
    
    // Default fallback language when DB is empty
    private val fallbackLanguage = Language(
        code = "en",
        name = "English", 
        nativeName = "English",
        greeting = "Hello",
        fallbackMessage = "Your information: {data}. Please try again or contact our helpline.",
        escalationMessage = "Your issue has been escalated. Ticket ID: {ticketId}",
        outOfScopeMessage = "I can only help with fintech and mutual fund queries.",
        flagEmoji = "🇬🇧",
        isActive = true,
        displayOrder = 1
    )

    fun refreshCache() {
        val languages = languageRepository.findAllActiveOrdered()
        if (languages.isNotEmpty()) {
            languageCache = languages.associateBy { it.code.lowercase() } + 
                           languages.associateBy { it.name.lowercase() }
            defaultLanguage = languages.find { it.code == "en" } ?: languages.firstOrNull()
            logger.info("Loaded ${languages.size} languages into cache")
        } else {
            logger.warn("No languages found in database, using fallback")
            languageCache = mapOf("en" to fallbackLanguage, "english" to fallbackLanguage)
            defaultLanguage = fallbackLanguage
        }
    }
    
    private fun ensureCacheLoaded() {
        if (languageCache == null) {
            refreshCache()
        }
    }

    fun getLanguage(codeOrName: String): Language {
        ensureCacheLoaded()
        return languageCache!![codeOrName.lowercase()] ?: defaultLanguage ?: fallbackLanguage
    }

    fun getAllActiveLanguages(): List<Language> {
        val languages = languageRepository.findAllActiveOrdered()
        return if (languages.isNotEmpty()) languages else listOf(fallbackLanguage)
    }

    fun getSupportedLanguageNames(): List<String> {
        return getAllActiveLanguages().map { it.name }
    }

    fun getSupportedLanguagesForPrompt(): String {
        val languages = getAllActiveLanguages()
        return languages.joinToString(", ") { "${it.name} (${it.nativeName})" }
    }

    fun getGreeting(languageCode: String): String {
        return getLanguage(languageCode).greeting
    }

    fun getFallbackMessage(languageCode: String, data: String = ""): String {
        val lang = getLanguage(languageCode)
        return lang.fallbackMessage.replace("{data}", data)
    }

    fun getEscalationMessage(languageCode: String, ticketId: String): String {
        val lang = getLanguage(languageCode)
        return lang.escalationMessage.replace("{ticketId}", ticketId)
    }

    fun getOutOfScopeMessage(languageCode: String): String {
        return getLanguage(languageCode).outOfScopeMessage
    }

    fun getLanguagesForFrontend(): List<Map<String, String>> {
        return getAllActiveLanguages().map { lang ->
            mapOf(
                "code" to lang.code,
                "name" to lang.name,
                "nativeName" to lang.nativeName,
                "flag" to lang.flagEmoji
            )
        }
    }
}
