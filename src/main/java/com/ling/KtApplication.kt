package com.ling

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 *@author LingLambda
 *@since 2025/3/7 14:01
 */
@SpringBootApplication
@MapperScan("com.ling.*.mapper")
open class KtApplication

fun main(args: Array<String>) {
    runApplication<KtApplication>(*args)
}