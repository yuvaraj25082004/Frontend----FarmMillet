package com.simats.farmmillet

data class OrderDetails(
    val orderId: String,
    val status: String,
    val consumer: String,
    val address: String,
    val phone: String,
    val productName: String,
    val price: String
)
