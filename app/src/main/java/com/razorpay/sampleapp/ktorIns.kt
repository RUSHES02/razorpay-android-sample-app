package com.razorpay.sampleapp

// In a new Kotlin file, e.g., KtorClient.kt
import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object KtorClient {

    // Replace "https://your_base_url.com/" with your server's base URL
    const val BASE_URL = "https://10.0.0.2:8080/" // Keep the trailing slash

    val instance: HttpClient by lazy {
        HttpClient(OkHttp) { // Using OkHttp engine
            // Configure JSON serialization
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }

            // Configure Logging (optional)
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("KtorLogger", message)
                    }
                }
                level = LogLevel.BODY // Log request and response bodies
            }

            // Default request configuration (e.g., base URL parts, headers)
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}