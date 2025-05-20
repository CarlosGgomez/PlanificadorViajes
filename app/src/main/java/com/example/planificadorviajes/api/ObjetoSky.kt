package com.example.planificadorviajes.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://sky-scrapper.p.rapidapi.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-rapidapi-key", "dc9d721a30msh410493048ee8586p18bcb9jsn07c07a225667")
                .addHeader("x-rapidapi-host", "sky-scrapper.p.rapidapi.com")
                .build()
            chain.proceed(request)
        }
        .build()

    val apiService: InterfazSky by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InterfazSky::class.java)
    }
}
