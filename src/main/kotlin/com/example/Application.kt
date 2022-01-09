package com.example

import com.example.customer.customerModule
import com.example.customer.customerRouting
import com.example.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.server.netty.*

import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun main(args: Array<String>): Unit = EngineMain.main(args)

val appModules = listOf(
    customerModule(),
)

fun Application.module(
    testing: Boolean = false,
    modules: List<Module> = appModules,
) {
    startKoin { modules(modules) }
    configureSerialization()
    customerRouting()
}
