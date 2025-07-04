package com.razorpay.sampleapp.networking

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, NetworkError> {
	val TAG = "ResponseToResult"
	Log.d(TAG, "Response: $response")
	return when (response.status.value) {
		in 200..299 -> {
			if (response.status == HttpStatusCode.NoContent) {
				Result.Success(Unit as T) // Assuming T is Unit
			} else {
				val data: T = try {
					response.body()
					
				} catch (e: SerializationException) {
					Log.d(TAG, "Response: $response")
					return Result.Error(NetworkError.Serialization)
				}
				Result.Success(data)
			}
		}
		in 400..599 -> {
			val errorBody: ApiErrorResponse? = try {
				response.body()
			} catch (e: SerializationException) {
				Log.d(TAG, "Response: $response")
				null // Fallback to a generic error if the error response can't be parsed
			}
			
			val errorMessage = errorBody?.message ?: "An error occurred (${response.status})"
			val networkError = when (response.status) {
				HttpStatusCode.BadRequest -> NetworkError.BadRequest(errorMessage)
				HttpStatusCode.Unauthorized -> NetworkError.Unauthorized(errorMessage)
				HttpStatusCode.Forbidden -> NetworkError.Forbidden(errorMessage)
				HttpStatusCode.NotFound -> NetworkError.NotFound(errorMessage)
				HttpStatusCode.UnprocessableEntity -> NetworkError.UnprocessableEntity(errorMessage)
				else -> NetworkError.CustomError(errorMessage, ErrorType.UNKNOWN)
			}
			
			Result.Error(networkError)
		}
		else -> {
			Result.Error(NetworkError.UnknownError)
		}
	}
}