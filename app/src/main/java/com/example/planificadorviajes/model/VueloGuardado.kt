package com.example.planificadorviajes.model

data class VueloGuardado(
    val origen: String = "",
    val destino: String = "",
    val salida: String = "",
    val llegada: String = "",
    val duracion: String = "Duracion no disponible",
    val escalas: String = "Escala no disponible",
    val precio: String = "",
    val aerolinea: String = "",
    val logoUrl: String = ""
)
