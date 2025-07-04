package com.razorpay.sampleapp.networking

import android.util.Log
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T > safeCall(
	execute: () -> HttpResponse
): Result<T, NetworkError> {
	
	val TAG = "SafeCall"
	val response = try {
		execute()
	}catch (e: UnresolvedAddressException){
		return Result.Error(NetworkError.NoInternet)
	}catch (e: SerializationException){
		return Result.Error(NetworkError.Serialization)
	}catch (e: Exception){
		coroutineContext.ensureActive()
		Log.e(TAG, "safeCall: ", e)
		return Result.Error(NetworkError.UnknownError)
	}
	Log.e(TAG, "safeCall: $response")
	
	return responseToResult(response)
}