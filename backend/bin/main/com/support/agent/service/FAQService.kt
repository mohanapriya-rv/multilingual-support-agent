package com.support.agent.service

import com.support.agent.entity.FAQ
import com.support.agent.repository.FAQRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FAQService(
    private val faqRepository: FAQRepository
) {
    private val logger = LoggerFactory.getLogger(FAQService::class.java)

    fun getRelevantFAQs(intentCategory: String, userMessage: String, language: String): List<FAQ> {
        // Extract keywords from user message
        val keywords = extractKeywords(userMessage)
        
        val faqs = mutableListOf<FAQ>()
        
        // Get FAQs by category
        faqs.addAll(faqRepository.findByCategoryOrderByPriority(intentCategory))
        
        // Get FAQs by keywords
        keywords.forEach { keyword ->
            faqs.addAll(faqRepository.findByKeywordContaining(keyword))
        }
        
        // Remove duplicates and limit to top 5
        return faqs.distinctBy { it.id }.take(5)
    }

    fun formatFAQsForPrompt(faqs: List<FAQ>, language: String): String {
        if (faqs.isEmpty()) return ""
        
        val sb = StringBuilder()
        sb.appendLine("\n=== RELEVANT FAQ KNOWLEDGE BASE ===")
        
        faqs.forEachIndexed { index, faq ->
            sb.appendLine("\nFAQ ${index + 1}:")
            
            // Use localized version if available
            when (language.lowercase()) {
                "hindi" -> {
                    sb.appendLine("Q: ${faq.questionHindi ?: faq.question}")
                    sb.appendLine("A: ${faq.answerHindi ?: faq.answer}")
                }
                "tamil" -> {
                    sb.appendLine("Q: ${faq.questionTamil ?: faq.question}")
                    sb.appendLine("A: ${faq.answerTamil ?: faq.answer}")
                }
                else -> {
                    sb.appendLine("Q: ${faq.question}")
                    sb.appendLine("A: ${faq.answer}")
                }
            }
        }
        
        sb.appendLine("\nUse the above FAQs to provide accurate, consistent answers.")
        return sb.toString()
    }

    private fun extractKeywords(message: String): List<String> {
        // Common fintech keywords to look for
        val keywordPatterns = listOf(
            "sip", "nav", "kyc", "pan", "aadhaar", "portfolio", "return", "tax",
            "elss", "redeem", "switch", "balance", "transaction", "payment",
            "failed", "refund", "block", "unblock", "statement", "nominee"
        )
        
        val messageLower = message.lowercase()
        return keywordPatterns.filter { messageLower.contains(it) }
    }
}
