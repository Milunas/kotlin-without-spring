package com.example.customer

import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)

class CustomerService(private val repository: CustomerRepository) {
    fun all(): List<Customer> = repository.findAll()
    fun create(customer: Customer) = repository.save(customer)
    fun one(id: String): Customer? = repository.findById(id)
    fun delete(id: String) = repository.deleteById(id)
}

class CustomerRepository(private val customerStorage: MutableList<Customer> = mutableListOf()) {
    fun findAll() = customerStorage
    fun save(customer: Customer) = customerStorage.add(customer)
    fun findById(id: String): Customer? = customerStorage.find { it.id == id }
    fun deleteById(id: String) = customerStorage.removeIf { customer -> customer.id == id }
}