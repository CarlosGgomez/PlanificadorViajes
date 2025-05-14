package com.example.planificadorviajes.api

import com.google.gson.annotations.SerializedName

data class RespuestaBusquedaVuelos(
    @SerializedName("data") val datos: DatosVuelos
)

data class DatosVuelos(
    @SerializedName("flightQuotes") val cotizaciones: CotizacionesVuelos
)

data class CotizacionesVuelos(
    @SerializedName("results") val resultados: List<CotizacionVuelo>
)

data class CotizacionVuelo(
    @SerializedName("type") val tipo: String,
    @SerializedName("id") val id: String,
    @SerializedName("content") val contenido: ContenidoVuelo
)

data class ContenidoVuelo(
    @SerializedName("direct") val esDirecto: Boolean,
    @SerializedName("price") val precio: String,
    @SerializedName("tripDuration") val duracionViaje: String,
    @SerializedName("outboundLeg") val vueloIda: TramoVuelo,
    @SerializedName("inboundLeg") val vueloVuelta: TramoVuelo
)

data class TramoVuelo(
    @SerializedName("originAirport") val aeropuertoOrigen: Aeropuerto,
    @SerializedName("destinationAirport") val aeropuertoDestino: Aeropuerto,
    @SerializedName("localDepartureDate") val fechaSalida: String
)

data class Aeropuerto(
    @SerializedName("name") val nombre: String
)
