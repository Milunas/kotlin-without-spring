package com.example

import com.example.customer.CustomerRepository
import com.example.customer.CustomerRepositoryInMemory
import com.example.customer.CustomerService
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

object E2EMisc {

    fun restartKoin() {
        stopKoin()
        startKoin {
            modules(
                org.koin.dsl.module {
                    single { CustomerRepositoryInMemory() as CustomerRepository }
                    single { CustomerService(get()) }
                })
        }
    }

    fun get(uri: String): (t: TestApplicationEngine) -> String =
        { t: TestApplicationEngine -> t.handleRequest(HttpMethod.Get, uri).response.content ?: "" }

    fun post(uri: String, body: String): (t: TestApplicationEngine) -> Unit = { t: TestApplicationEngine ->
        t.handleRequest(HttpMethod.Post, uri) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }
    }

    inline fun <reified T> decode(body: String) = Json.decodeFromString<T>(body)

    inline fun <reified T> encode(body: T) = Json.encodeToString(body)
}