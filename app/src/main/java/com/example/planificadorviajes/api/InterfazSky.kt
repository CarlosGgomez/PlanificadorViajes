package com.example.planificadorviajes.api


import com.example.planificadorviajes.model.RespuestaBusquedaAereopuerto
import com.example.planificadorviajes.model.RespuestaBusquedaVuelos
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface InterfazSky {
    @GET("api/v2/flights/searchFlights")
    suspend fun buscarVuelos(
        @Query("originSkyId") originSkyId: String,
        @Query("destinationSkyId") destinationSkyId: String,
        @Query("originEntityId") originEntityId: String,
        @Query("destinationEntityId") destinationEntityId: String,
        @Query("date") date: String,
        @Query("returnDate") returnDate: String = "",
        @Query("cabinClass") cabinClass: String = "economy",
        @Query("adults") adults: Int = 1,
        @Query("sortBy") sortBy: String = "best",
        @Query("currency") currency: String = "USD",
        @Query("market") market: String = "en-US",
        @Query("countryCode") countryCode: String = "US"
    ): Response<RespuestaBusquedaVuelos>

    @GET("api/v1/flights/searchAirport")
    suspend fun buscarAeropuerto(
        @Query("query") ciudad: String
    ): Response<RespuestaBusquedaAereopuerto>

}
