package com.example.planificadorviajes.api
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface InterfazSky {

    @Headers(
        "x-rapidapi-host: skyscanner89.p.rapidapi.com",
        "x-rapidapi-key: dc9d721a30msh410493048ee8586p18bcb9jsn07c07a225667"
    )

    //pilla la info pero con codigo IATA
    @GET("flights/roundtrip/list")
    suspend fun buscarVuelos(
        @Query("origin") origen: String,
        @Query("originId") idOrigen: String,
        @Query("destination") destino: String,
        @Query("destinationId") idDestino: String,
        @Query("departDate") fechaIda: String,
        @Query("returnDate") fechaVuelta: String
    ): RespuestaBusquedaVuelos

    //con esto traduciremos la ciudad a codigo iata
    @GET("flights/auto-complete")
    suspend fun autocompletar(
        @Query("query") ciudad: String,
        @Header("x-rapidapi-host") host: String = "skyscanner89.p.rapidapi.com",
        @Header("x-rapidapi-key") apiKey: String = "dc9d721a30msh410493048ee8586p18bcb9jsn07c07a225667"
    ): RespuestaAutocompletado

}
