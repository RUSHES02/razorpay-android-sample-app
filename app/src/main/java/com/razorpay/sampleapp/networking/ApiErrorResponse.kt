package com.razorpay.sampleapp.networking

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
	val statusCode: Int,
	val message: String,
	val error: String? = null,
	val timestamp: String? = null,
	val path: String? = null
)
