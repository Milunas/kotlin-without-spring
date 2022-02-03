package com.example

import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

import kotlinx.serialization.json.Json

object E2EMisc {

    fun setup(application: Application) {
        val port = application.environment.config.property("database.port").getString().toInt()
        val instance = MongodStarter.getDefaultInstance()
        val config = MongodConfig.builder()
            .version(Version.Main.PRODUCTION)
            .net(Net(port, Network.localhostIsIPv6())).build()

        instance.prepare(config).start()
    }

    fun get(uri: String, token: String): (t: TestApplicationEngine) -> String =
        { t: TestApplicationEngine -> t.handleRequest(HttpMethod.Get, uri) {
            addHeader(HttpHeaders.Authorization, token)
        }.response.content ?: "" }

    fun post(uri: String, body: String, token: String): (t: TestApplicationEngine) -> Unit = { t: TestApplicationEngine ->
        t.handleRequest(HttpMethod.Post, uri) {
            addHeader(HttpHeaders.Authorization, token)
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }
    }

    inline fun <reified T> decode(body: String) = Json.decodeFromString<T>(body)

    inline fun <reified T> encode(body: T) = Json.encodeToString(body)
}