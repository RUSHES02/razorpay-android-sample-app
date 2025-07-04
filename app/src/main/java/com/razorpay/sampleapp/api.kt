package com.razorpay.sampleapp

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.setBody

class Api(
    private val httpClient: HttpClient,
) {
    suspend fun paymentOrder(id: String, request: CreatePaymentOrderRequest): OrderResponse {
        return safeCall<OrderResponse> {
            httpClient.patch(
                urlString = constructUrl("/reservations/${id}/status")
            ) {
                setBody(request)
            }
        }
    }
}