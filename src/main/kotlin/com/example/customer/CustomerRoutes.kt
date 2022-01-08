package com.example.customer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

/** Koin introduces extension function enabling to use injection inside ApplicationCall,
 *  but for some reason unknown to me it couldn't be used inside Route */
val customerService: CustomerService by KoinJavaComponent.inject(CustomerService::class.java)

fun Route.customerRoutes() {
    route("/customer") {

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

fun Application.customerRouting() {
    routing {
        customerRoutes()
    }
}