package com.razorpay.sampleapp.networking

sealed class NetworkError(
	open val message: String,
	val type: ErrorType
) : Error {
	object NoInternet : NetworkError("No internet connection.", ErrorType.NO_INTERNET)
	object Serialization : NetworkError("Serialization error occurred.", ErrorType.SERIALIZATION)
	object UnknownError : NetworkError("An unknown error occurred.", ErrorType.UNKNOWN)
	
	data class BadRequest(override val message: String) : NetworkError(message, ErrorType.BAD_REQUEST)
	data class Unauthorized(override val message: String) : NetworkError(message, ErrorType.UNAUTHORIZED)
	data class Forbidden(override val message: String) : NetworkError(message, ErrorType.FORBIDDEN)
	data class NotFound(override val message: String) : NetworkError(message, ErrorType.NOT_FOUND)
	data class UnprocessableEntity(override val message: String) : NetworkError(message, ErrorType.UNPROCESSABLE_ENTITY)
	
	data class ServerError(override val message: String) : NetworkError(message, ErrorType.SERVER_ERROR)
	
	data class CustomError(
		override val message: String,
		val customType: ErrorType
	) : NetworkError(message, customType)
}
