package com.example

import com.example.customer.customerModule
import com.example.customer.customerRoutes
import com.example.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import org.koin.core.context.startKoin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {

        startKoin {
            modules(customerModule())
        }

        configureSerialization()
        customerRoutes()
    }.start(wait = true)
}
