package com.mmetzner.vehiclemaintenance.feature.vehicle.domain

data class Vehicle(
    val plate: String,
    val model: String,
    val brand: String,
    val year: Int,
    val maintenances: List<Maintenance>? = null
)