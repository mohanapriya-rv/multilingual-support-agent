package com.support.agent

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SupportAgentApplication

fun main(args: Array<String>) {
    runApplication<SupportAgentApplication>(*args)
}
