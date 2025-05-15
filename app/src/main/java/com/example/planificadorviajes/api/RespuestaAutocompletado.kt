package com.example.planificadorviajes.api



import com.google.gson.annotations.SerializedName

data class RespuestaAutocompletado(
    @SerializedName("inputSuggest") val sugerencias: List<Sugerencia>
)

data class Sugerencia(
    @SerializedName("navigation") val navegacion: Navegacion
)

data class Navegacion(
    @SerializedName("relevantFlightParams") val parametros: ParametrosVuelo
)

data class ParametrosVuelo(
    @SerializedName("skyId") val skyId: String,
    @SerializedName("entityId") val entityId: String
)
