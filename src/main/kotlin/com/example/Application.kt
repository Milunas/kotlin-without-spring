package com.example

import com.example.customer.customerModule
import com.example.customer.customerRouting
import com.example.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.server.netty.*

import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

    startKoin {
        modules(customerModule())
    }

    configureSerialization()
    customerRouting()
}
