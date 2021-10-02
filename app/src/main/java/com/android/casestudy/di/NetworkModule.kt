package com.avinash.currencyconverter.di

import com.android.casestudy.data.SwbtAPI
import com.android.casestudy.data.remote.ErrorHandleInterceptor
import com.android.casestudy.data.remote.NetworkStateHandler
import com.android.casestudy.util.Constants.API_TIMEOUT
import com.android.casestudy.util.Constants.baseURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    @JvmStatic
    fun buildRetrofitInstance(errorHandleInterceptor: ErrorHandleInterceptor): SwbtAPI {

        val builder = OkHttpClient.Builder()
        builder.readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        builder.connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)

        val requestInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(requestInterceptor).addInterceptor(errorHandleInterceptor)

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(SwbtAPI::class.java)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun buildErrorHandlingInterceptor() : ErrorHandleInterceptor = ErrorHandleInterceptor()

    @Provides
    @Singleton
    @JvmStatic
    fun buildNetworkStateHandler(): NetworkStateHandler = NetworkStateHandler()


}