package com.example.customer

import org.koin.dsl.module

/** The place where we declare beans */
fun customerModule() = module {
    single { CustomerRepositoryImpl() as CustomerRepository }
    single { CustomerService(get()) }
}