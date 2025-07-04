package com.razorpay.sampleapp

// In a new Kotlin file, e.g., NetworkModels.kt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Data class for the request body your server expects (if any)
@Serializable
data class CreatePaymentOrderRequest(
    val amount: Long, // in paise
    val currency: String,
    val order_id: String? = null,
    val partial_payment: Boolean = false,
    val notes: Map<String, String>? = null
)

// Data class for the response your server sends
@Serializable
data class OrderResponse(
    @SerialName("orderId") val orderId: String
    // Add other fields your server might return
)