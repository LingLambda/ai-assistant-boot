package com.ling.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 *@author LingLambda
 *@since 2025/3/18 11:18
 */
@Configuration
open class CommonProperties {
    @Value("\${google-search.api-key}")
    val apiKey: String? = null

    @Value("\${google-search.engine-id}")
    val engineId: String? = null
}