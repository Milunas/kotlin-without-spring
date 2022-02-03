package com.example.customer

import com.example.E2EMisc.decode
import com.example.E2EMisc.encode
import com.example.E2EMisc.get
import com.example.E2EMisc.post
import com.example.E2EMisc.setup
import com.typesafe.config.ConfigFactory
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import junit.framework.Assert.assertEquals
import org.junit.Test

class CustomerTest {
    
    val user = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5uYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9" +
            ".NuYB70HCz4eUV6v-a2PWfHJJT_ZPQJk2tvOTt5uFZM8"

    private val testEnv: ApplicationEngineEnvironment  = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
    }

    @Test
    fun createAndListCustomer() = withApplication(testEnv) {
        setup(application)

        // given
        val newCustomer = Customer("l", "m", "lm@com")

        // when
        post("/customer", encode(newCustomer), user).invoke(this)

        // and
        val getCustomers = get("/customer", user).invoke(this)
        val customers = decode<List<Customer>>(getCustomers)

        // then
        assertEquals(customers.size, 1)
    }
}