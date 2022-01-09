package com.example.customer

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val firstName: String,
    val lastName: String,
    val email: String,
    val id: String? = null,
)
