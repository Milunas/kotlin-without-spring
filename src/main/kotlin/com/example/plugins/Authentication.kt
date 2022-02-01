package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.jwt.*
import io.ktor.auth.Authentication.Feature as Feature

fun Application.configureAuth(secret: String) {
    install(Feature) {
        jwt("auth-jwt") {

            verifier(JWT.require(Algorithm.HMAC256(secret)).build())

            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}