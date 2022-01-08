package com.example.customer

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerByIdRoute() {

    get("/customer/{id}") {
        val id: String? = call.parameters["id"]
        call.respondText("Hello customer: $id")
    }
}

fun Application.customerRoutes() {
    routing {
        customerByIdRoute()
    }
}