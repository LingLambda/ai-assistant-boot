package com.ling.chat.tools

import java.time.LocalDateTime
import org.springframework.ai.tool.annotation.Tool
import org.springframework.context.i18n.LocaleContextHolder

class DateTimeTools {
    @Tool(description = "Get the current date and time in the user's timezone")
    fun getCurrentDateTime(): String {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString()
    }
}
