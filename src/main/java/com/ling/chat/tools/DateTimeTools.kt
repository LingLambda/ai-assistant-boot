package com.ling.chat.tools

import org.springframework.ai.tool.annotation.Tool
import org.springframework.context.i18n.LocaleContextHolder
import java.time.LocalDateTime

class DateTimeTools {
    @Tool(description = "Get the current date and time in the user's timezone")
    fun getCurrentDateTime(): String {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString()
    }
}
