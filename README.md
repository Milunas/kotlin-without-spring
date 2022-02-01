### Goal

Bored with the constant use of spring in commercial projects, \
I decided to investigate the use of an alternative tech stack.

Requirements for alternative:

* Easy to implement endpoints & test HTTP layer
* Way to inject dependencies into classes
* Alternative for Spring repositories with easy saving
* Easy to implement JWT authorization
* Possibility of working with WebSockets
* Handling environment variables  
* Possibility of run on container

### Alternative stack

* Ktor for HTTP
* Koin for DI
* KMongo for working with database

### Start up

Instruction how to easily start with Ktor: [Link](https://ktor.io/docs/intellij-idea.html#install_plugin)

1) Install Ktor
   plugin: [Link](https://plugins.jetbrains.com/plugin/16008-ktor?_ga=2.119157257.1475561129.1641494389-2002089144.1641494389&_gl=1*10ouy88*_ga*MjAwMjA4OTE0NC4xNjQxNDk0Mzg5*_ga_VCMCSM1ZZ7*MTY0MTU3MDgyMS4yLjEuMTY0MTU3MzEwOC4w)

2) Create new project in IntelliJ
   ![img.png](doc/img.png)

You can also manipulate existing gradle (or maven) configuration
https://ktor.io/docs/gradle.html#create-new-gradle-project

### HTTP Layer

Routing is organized in a tree with a recursive matching system that is capable of handling quite complex rules for
request processing. The Tree is built with nodes that contain selector and optional handler. Selectors are used to pick
a route based on request. Source: https://ktor.io/docs/resolution-algorithms.html

### Dependency Injection

Dependency injection is handled differently in functional languages, but due to Spring habits we don't want to
experience shock therapy. Therefore, we will use the Koin library to build something "Spring-ish".

### E2E Test

Ktor providing `withTestApplication`

```kotlin
    @Test
fun createAndListCustomer() = withTestApplication(Application::module) {
        //..
    }
```

Where you can use `TestApplicationEngine`
to ["hook directly into internal mechanisms and processes an application call"](https://ktor.io/docs/testing.html)

```kotlin
    fun get(uri: String): (t: TestApplicationEngine) -> String =
    { t: TestApplicationEngine ->
        t.handleRequest(HttpMethod.Get, uri).response.content
    }
```

I pulled out http calls & json converting methods to [E2EMisc](src/test/kotlin/com/example/E2EMisc.kt) so I can have E2E
test quite readable

```kotlin
    // given
val newCustomer = Customer("1", "l", "m", "lm@com")

// when
post("/customer", encode(newCustomer)).invoke(this)

// and
val getCustomers = get("/customer").invoke(this)
val customers = decode<List<Customer>>(getCustomers)

// then
assertEquals(customers.size, 1)
```

### GitHub actions

You can use GitHub actions for CI purpose just like here: [code](.github/workflows/test.yaml)

### KMongo

Setup mongo locally with docker-compose \
`docker-compose -f infrastructure/docker-compose.yaml up -d`

I'm using kotlinx serialization so stay that way also with Mongo \
https://litote.org/kmongo/quick-start/#with-kotlinxserialization

### Testing KMongo

Different from Spring for testing application with embedded database \
you need to implement code for flapdoodle configuration

example: https://github.com/Litote/kmongo/tree/master/kmongo-flapdoodle

So I decided to use a InMemory database for testing purpose
```kotlin
class CustomerRepositoryInMemory : CustomerRepository {
    private val customerStorage = mutableListOf<Customer>()

    override fun findAll(): List<Customer> = customerStorage
    override fun save(customer: Customer) = customerStorage.add(customer)
    override fun findById(id: String): Customer? = customerStorage.find { it.id == id }
    override fun deleteById(id: String): Boolean = customerStorage.removeIf { it.id == id }
}
```

It's easy to create Koin Customer module for testing purpose with this InMemory database

```kotlin
 private val customerModule = listOf(org.koin.dsl.module {
        single { CustomerRepositoryInMemory() }
        single { CustomerService(get()) }
    })
```

Use this with Ktor test
```kotlin
withTestApplication({ module(true, customerModule) }) {}
```

### JWT
I created new extension function for application called `configureAuth`

This function installing new Authentication.Feature where I can define new jwt configuration, named somehow.
By this name you can then use proper auth configurations in routing:

```kotlin
 route("/customer") {
        authenticate("auth-jwt") {
```

If you are using HOCON file (more about that here: https://ktor.io/docs/configurations.html#hocon-file) \
you can easily add new environment variables:

_src/main/resources/application.conf_
```
jwt {
    secret = "secret"
}
```

This JWT secret is fetched in `Application.module()` from properties like this: \
`    
val secret = environment.config.property("jwt.secret").getString()
`


Unfortunately I couldn't inject properties into tests as was suggested here: https://ktor.io/docs/testing.html#hocon
```kotlin
    private val testEnv: ApplicationEngineEnvironment  = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application.conf"))
    }
    
    @Test
    fun createAndListCustomer() = withTestApplication(
        { module(customerModule) },
        { testEnv }
    )
```

I see easy workaround for that - I can just pass test properties somehow into  application module,
but better first to search deeper for proper way of doing it.