package com.ling.chat.tools

import com.google.gson.annotations.SerializedName
import com.ling.common.config.CommonProperties
import org.springframework.ai.tool.annotation.Tool
import org.springframework.web.reactive.function.client.WebClient


/**
 *@author LingLambda
 *@since 2025/3/18 10:48
 */
class SearchTools(private val commonProperties: CommonProperties) {

    @Tool(description = "Use google search keyWord")
    fun googleSearch(keyWord: String): CustomSearchResponse {
        val webClient = WebClient.create("https://customsearch.googleapis.com")
        val apiKey = commonProperties.apiKey
        val engineId = commonProperties.engineId
        requireNotNull(apiKey) { "googleApi is null" }
        requireNotNull(engineId) { "engineId is null" }
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/customsearch/v1").queryParam("key", apiKey)
                    .queryParam("q", keyWord)
                    .queryParam("cx", engineId).queryParam("num", "4").build()
            }
            .retrieve()
            .bodyToMono(CustomSearchResponse::class.java)
            .block()
        requireNotNull(response)
        return response
    }
}

data class CustomSearchResponse(
    val kind: String?,
    val url: Url?,
    val queries: Queries?,
    val context: Context?,
    val searchInformation: SearchInformation?,
    val items: List<Item>?
)

data class Url(
    val type: String?,
    val template: String?
)

data class Queries(
    val request: List<Request>?,
    val nextPage: List<Request>?
)

data class Request(
    val title: String?,
    val totalResults: String?,
    val searchTerms: String?,
    val count: Int?,
    val startIndex: Int?,
    val inputEncoding: String?,
    val outputEncoding: String?,
    val safe: String?,
    val cx: String?
)

data class Context(
    val title: String?
)

data class SearchInformation(
    val searchTime: Double?,
    val formattedSearchTime: String?,
    val totalResults: String?,
    val formattedTotalResults: String?
)

data class Item(
    val kind: String?,
    val title: String?,
    val htmlTitle: String?,
    val link: String?,
    val displayLink: String?,
    val snippet: String?,
    val htmlSnippet: String?,
    val formattedUrl: String?,
    val htmlFormattedUrl: String?,
    val pagemap: Pagemap?
)

data class Pagemap(
    val hcard: List<HCard>?,
    val metatags: List<Metatag>?
)

data class HCard(
    val url_text: String?,
    val bday: String?,
    val fn: String?,
    val nickname: String?,
    val label: String?,
    val url: String?
)

data class Metatag(
    val referrer: String?,
    @SerializedName("og:image") val ogImage: String?,
    val themeColor: String?,
    @SerializedName("og:image:width") val ogImageWidth: String?,
    @SerializedName("og:type") val ogType: String?,
    val viewport: String???,
    @SerializedName("og:title") val ogTitle: String?,
    @SerializedName("og:image:height") val ogImageHeight: String?,
    @SerializedName("format-detection") val formatDetection: String?
)