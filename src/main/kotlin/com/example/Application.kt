package com.example

import com.example.customer.customerModule
import com.example.customer.customerRouting
import com.example.plugins.configureAuth
import com.example.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val secret = environment.config.property("jwt.secret").getString()
    val dbPort = environment.config.property("database.port").getString().toInt()
    startKoin { modules(
        listOf(
            customerModule(dbPort)
        )
    ) }
    configureSerialization()
    configureAuth(secret)
    customerRouting()
}
