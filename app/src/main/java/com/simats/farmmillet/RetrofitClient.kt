package com.simats.farmmillet

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TAG = "RetrofitClient"
    
    // Default BASE_URL for local development. 
    // In production, this would be your server URL.
    private var BASE_URL = "http://192.168.0.15/Millets/api/"
    
    private var authToken: String? = null

    fun setBaseUrl(url: String) {
        BASE_URL = if (url.endsWith("/")) url else "$url/"
        _instance = null // Reset instance to recreate with new URL
    }

    fun setToken(token: String?) {
        authToken = token
    }

    private val authInterceptor = okhttp3.Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        requestBuilder.addHeader("Content-Type", "application/json")
        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(TAG, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private var _instance: ApiService? = null

    val instance: ApiService
        get() {
            if (_instance == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                _instance = retrofit.create(ApiService::class.java)
            }
            return _instance!!
        }
}
