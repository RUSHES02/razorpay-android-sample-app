package com.razorpay.sampleapp.networking

import com.razorpay.sampleapp.BuildConfig


fun constructUrl(url: String): String {
	return when  {
		url.contains(BuildConfig.BASE_URL) -> url
		url.startsWith("/") -> BuildConfig.BASE_URL + url
		else -> BuildConfig.BASE_URL + "/" + url

	}
	return ""
}