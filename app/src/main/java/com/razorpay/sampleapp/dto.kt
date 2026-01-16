package com.razorpay.sampleapp

// In a new Kotlin file, e.g., NetworkModels.kt
import com.razorpay.sampleapp.networking.Data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Data class for the request body your server expects (if any)
@Serializable
data class CreatePaymentOrderRequest(
    val amount: Long, // in paise
    val currency: String,
    val orderId: String? = null,
    val partialPayment: Boolean = false,
//    val notes: Map<String, String>? = null
)

// Data class for the response your server sends

@Serializable
data class OrderResponse(
    val `data`: OrderDto,
    val message: String
)

@Serializable
data class OrderDto(
    @SerialName("id") val id: String,
    val entity: String,
    val amount: Int,
    @SerialName("amount_paid")
    val amountPaid: Int,
    @SerialName("amount_due")
    val amountDue: Int,
    val currency: String,
    val receipt: String,
    @SerialName("offer_id")
    val offerId: String?,
    val status: String,
    val attempts: Int,
    @SerialName("created_at")
    val createdAt: Long
)

@Serializable
data class CreatePaymentResponse(
    val amount: Int,
    val currency: String,
    val orderId: String,
    val paymentOrderId: String
)
