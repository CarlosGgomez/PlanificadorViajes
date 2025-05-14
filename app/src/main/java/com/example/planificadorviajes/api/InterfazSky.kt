package com.example.planificadorviajes.api
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface InterfazSky {

    @Headers(
        "x-rapidapi-host: skyscanner89.p.rapidapi.com",
        "x-rapidapi-key: dc9d721a30msh410493048ee8586p18bcb9jsn07c07a225667"
    )
    @GET("flights/roundtrip/list")
    suspend fun buscarVuelos(
        @Query("origin") origen: String,
        @Query("originId") idOrigen: String,
        @Query("destination") destino: String,
        @Query("destinationId") idDestino: String,
        @Query("departDate") fechaIda: String,
        @Query("returnDate") fechaVuelta: String
    ): RespuestaBusquedaVuelos
}
