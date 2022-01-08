package com.example.customer

import org.koin.dsl.module

/** The place where we declare beans */
fun customerModule() = module {
    single { CustomerRepository() }
    single { CustomerService(get()) }
}