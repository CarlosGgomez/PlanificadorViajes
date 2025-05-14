package com.example.planificadorviajes.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ObjetoSky {
    private const val BASE_URL = "https://skyscanner89.p.rapidapi.com/"

    private val retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofit: InterfazSky = retrofitInstance.create(InterfazSky::class.java)
}
