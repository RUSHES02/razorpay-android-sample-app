package com.razorpay.sampleapp

import com.razorpay.sampleapp.networking.NetworkError
import com.razorpay.sampleapp.networking.Result
import com.razorpay.sampleapp.networking.constructUrl
import com.razorpay.sampleapp.networking.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class Api(
    private val httpClient: HttpClient,
) {
    suspend fun paymentOrder(request: CreatePaymentOrderRequest): Result<OrderResponse, NetworkError> {
        return safeCall<OrderResponse> {
            httpClient.post(
                urlString = constructUrl("/payment")
            ) {
                setBody(request)
            }
        }
    }
}