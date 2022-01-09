package com.example.customer

import org.litote.kmongo.*

interface CustomerRepository {
    fun findAll(): List<Customer>
    fun save(customer: Customer): Boolean
    fun findById(id: String): Customer?
    fun deleteById(id: String): Boolean
}

class CustomerRepositoryImpl : CustomerRepository{
    private val client = KMongo.createClient()
    private val database = client.getDatabase("test")
    private val col = database.getCollection<Customer>()

    override fun findAll(): List<Customer> = col.find().toList()
    override fun save(customer: Customer) = col.insertOne(customer).wasAcknowledged()
    override fun findById(id: String): Customer? = col.findOne(Customer::id eq id)
    override fun deleteById(id: String) = col.deleteOneById(id).wasAcknowledged()
}

class CustomerRepositoryInMemory : CustomerRepository {
    private val customerStorage = mutableListOf<Customer>()

    override fun findAll(): List<Customer> = customerStorage
    override fun save(customer: Customer) = customerStorage.add(customer)
    override fun findById(id: String): Customer? = customerStorage.find { it.id == id }
    override fun deleteById(id: String): Boolean = customerStorage.removeIf { it.id == id }
}

