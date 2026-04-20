package com.support.agent.controller

import com.support.agent.service.LanguageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/languages")
@CrossOrigin(origins = ["*"])
class LanguageController(
    private val languageService: LanguageService
) {
    
    @GetMapping
    fun getAllLanguages(): ResponseEntity<List<Map<String, String>>> {
        return ResponseEntity.ok(languageService.getLanguagesForFrontend())
    }
    
    @GetMapping("/refresh")
    fun refreshLanguageCache(): ResponseEntity<Map<String, String>> {
        languageService.refreshCache()
        return ResponseEntity.ok(mapOf("status" to "Language cache refreshed"))
    }
}
