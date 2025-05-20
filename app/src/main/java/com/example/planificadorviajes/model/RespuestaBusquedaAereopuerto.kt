package com.example.planificadorviajes.model



data class RespuestaBusquedaAereopuerto(
    val status: Boolean,
    val timestamp: Long,
    val data: List<AeropuertoData>
)

data class AeropuertoData(
    val presentation: Presentation?,
    val navigation: Navigation?,
    val relevantFlightParams: RelevantFlightParams?
)

data class Presentation(
    val title: String,
    val suggestionTitle: String,
    val subtitle: String
)

data class Navigation(
    val entityId: String,
    val entityType: String,
    val localizedName: String
)

data class RelevantFlightParams(
    val skyId: String,
    val entityId: String,
    val flightPlaceType: String,
    val localizedName: String
)
