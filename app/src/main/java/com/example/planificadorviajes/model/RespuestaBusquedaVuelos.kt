package com.example.planificadorviajes.model

data class RespuestaBusquedaVuelos(
    val status: Boolean,
    val timestamp: Long,
    val data: Data
)

data class Data(
    val itineraries: List<Itinerary>
)

data class Itinerary(
    val id: String,
    val price: Price,
    val legs: List<Leg>
)

data class Price(
    val raw: Double,
    val formatted: String
)

data class Leg(
    val id: String,
    val origin: Location,
    val destination: Location,
    val departure: String,
    val arrival: String,
    val durationInMinutes: Int,
    val stopCount: Int,
    val carriers: Carriers
)

data class Location(
    val id: String,
    val name: String,
    val displayCode: String,
    val city: String,
    val country: String
)

data class Carriers(
    val marketing: List<Carrier>
)

data class Carrier(
    val id: Int,
    val name: String,
    val logoUrl: String
)
