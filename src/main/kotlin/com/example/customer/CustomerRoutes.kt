package com.example.customer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

/** Koin introduces extension function enabling to use injection inside ApplicationCall,
 *  but for some reason unknown to me it couldn't be used inside Route */
val customerService: CustomerService by KoinJavaComponent.inject(CustomerService::class.java)

fun Route.customerByIdRoute() {

    get("/customer") {
        call.respond(HttpStatusCode.OK, customerService.all())
    }

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