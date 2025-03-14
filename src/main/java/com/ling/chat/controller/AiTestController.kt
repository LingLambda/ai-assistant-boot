package com.ling.chat.controller

import com.ling.chat.tools.BookTools
import com.ling.chat.tools.DateTimeTools
import com.ling.chat.tools.WeatherTools
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.InMemoryChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.web.bind.annotation.*

/**
 *@author LingLambda
 *@since 2025/3/14 15:33
 */
@RestController
@RequestMapping("chat/test")
@CrossOrigin
class AiTestController(
    private var chatClient: ChatClient?,
    private var chatClientBuild: ChatClient.Builder?,
    private var vectorStore: VectorStore?
) {
    private var inMemoryChatMemory: InMemoryChatMemory? = null

    init {
        inMemoryChatMemory = InMemoryChatMemory()
    }

    @GetMapping("tools")
    fun chatTime(message:String): String? {
        return chatClient?.prompt(message)?.tools(DateTimeTools(), WeatherTools(),BookTools())?.call()?.content()
    }

}