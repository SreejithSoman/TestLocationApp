package com.example.testlocationapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object ApiClient {

    private val API_BASE_URL = "https://api-staging.pmlo.co/"
//    private var servicesApiInterface: ServicesApiInterface? = null

    fun build(): ApiManager.ServicesApiInterface? {

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.writeTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(interceptor())

        val builder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
        }

        val servicesApiInterface : ApiManager.ServicesApiInterface by lazy {
            builder.build()
                .create(ApiManager.ServicesApiInterface::class.java)
        }

        return servicesApiInterface
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}