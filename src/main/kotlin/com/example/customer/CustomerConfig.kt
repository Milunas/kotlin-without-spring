package com.example.customer

import org.koin.dsl.module

/** The place where we declare beans */
fun customerModule(dbPort: Int) = module {
    single { CustomerRepositoryImpl(dbPort) as CustomerRepository }
    single { CustomerService(get()) }
}