package com.android.casestudy.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ErrorHandleInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        runCatching {
            val response = chain.proceed(request)
            if (response.isSuccessful.not()) {
                throw ApiException()
            }
            return response
        }.getOrElse {
            throw ApiException()
        }
    }
}
