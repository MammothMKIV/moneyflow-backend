package io.moneyflow.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoneyflowApplication

fun main(args: Array<String>) {
    runApplication<MoneyflowApplication>(*args)
}
