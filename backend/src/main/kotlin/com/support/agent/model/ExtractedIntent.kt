package com.support.agent.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ExtractedIntent(
    @JsonProperty("detected_language")
    val detectedLanguage: String = "english",
    
    @JsonProperty("intent_category")
    val intentCategory: String = "out_of_scope",
    
    @JsonProperty("intent_type")
    val intentType: String? = null,
    
    val confidence: Double = 0.0,
    
    val entities: IntentEntities = IntentEntities(),
    
    @JsonProperty("requires_escalation")
    val requiresEscalation: Boolean = false,
    
    @JsonProperty("escalation_reason")
    val escalationReason: String? = null,
    
    @JsonProperty("is_in_scope")
    val isInScope: Boolean = true,
    
    @JsonProperty("out_of_scope_reason")
    val outOfScopeReason: String? = null
)

data class IntentEntities(
    @JsonProperty("user_id")
    val userId: String? = null,
    
    @JsonProperty("transaction_id")
    val transactionId: String? = null,
    
    val merchant: String? = null,
    
    val amount: Double? = null,
    
    @JsonProperty("fund_name")
    val fundName: String? = null,
    
    @JsonProperty("sip_id")
    val sipId: String? = null
)
