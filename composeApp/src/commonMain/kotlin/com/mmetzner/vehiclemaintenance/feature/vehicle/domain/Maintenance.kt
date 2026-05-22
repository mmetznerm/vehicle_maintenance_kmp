package com.mmetzner.vehiclemaintenance.feature.vehicle.domain

data class Maintenance(
    val id: String,
    val date: String,
    val description: String,
    val workshopName: String?,
    val photoUrls: List<String>? = null,
    val isPendingSync: Boolean = false
)