package com.ling

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 *@author LingLambda
 *@since 2025/3/7 14:01
 */
@SpringBootApplication
open class KtApplication

fun main(args: Array<String>) {
    runApplication<KtApplication>(*args)
}