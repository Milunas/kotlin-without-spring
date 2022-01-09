package com.example.customer

import com.example.E2EMisc.get
import com.example.E2EMisc.post
import com.example.E2EMisc.decode
import com.example.E2EMisc.encode
import com.example.module
import io.ktor.application.*
import io.ktor.server.testing.*
import junit.framework.Assert.assertEquals
import org.junit.Test

class CustomerTest {

    @Test
    fun createAndListCustomer() = withTestApplication(Application::module) {
        // given
        val newCustomer = Customer("1", "l", "m", "lm@com")

        // when
        post("/customer", encode(newCustomer)).invoke(this)

        // and
        val getCustomers = get("/customer").invoke(this)
        val customers = decode<List<Customer>>(getCustomers)

        // then
        assertEquals(customers.size, 1)
    }
}