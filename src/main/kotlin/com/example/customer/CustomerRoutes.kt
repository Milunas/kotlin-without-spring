package com.example.customer

import io.ktor.http.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.customerRoutes() {

    /** We can inject beans into Route thanks to Koin extension methods */
    val customerService: CustomerService by inject()

    route("/customer") {
        authenticate("auth-jwt") {

            get {
                call.respond(HttpStatusCode.OK, customerService.all())
            }

            get("{id}") {
                /** Spring doing this out of box*/
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "Missing or malformed id",
                    status = HttpStatusCode.BadRequest
                )

                val customer = customerService.one(id) ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )

                call.respond(customer)
            }

            post {
                val customer = call.receive<Customer>()
                customerService.create(customer)
                call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
            }

            delete("{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (customerService.delete(id)) {
                    call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("Not Found", status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}

fun Application.customerRouting() {
    routing {
        customerRoutes()
    }
}