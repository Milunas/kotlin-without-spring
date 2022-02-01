package com.example.customer

import com.example.E2EMisc.decode
import com.example.E2EMisc.encode
import com.example.E2EMisc.get
import com.example.E2EMisc.post
import com.example.module
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class CustomerTest {

    private val customerModule = listOf(org.koin.dsl.module {
        single { CustomerRepositoryInMemory() }
        single { CustomerService(get()) }
    })

    private val testEnv: ApplicationEngineEnvironment  = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
    }

    @Ignore("Problem with injecting configuration")
    @Test
    fun createAndListCustomer() = withTestApplication(
        { module(customerModule) },
        { testEnv }
    ) {

        // given
        val newCustomer = Customer("l", "m", "lm@com")

        // when
        post("/customer", encode(newCustomer)).invoke(this)

        // and
        val getCustomers = get("/customer").invoke(this)
        val customers = decode<List<Customer>>(getCustomers)

        // then
        assertEquals(customers.size, 1)
    }
}