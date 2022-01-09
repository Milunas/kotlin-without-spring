package com.example.customer

class CustomerService(private val repository: CustomerRepository) {
    fun all(): List<Customer> = repository.findAll()
    fun create(customer: Customer) = repository.save(customer)
    fun one(id: String): Customer? = repository.findById(id)
    fun delete(id: String) = repository.deleteById(id)
}