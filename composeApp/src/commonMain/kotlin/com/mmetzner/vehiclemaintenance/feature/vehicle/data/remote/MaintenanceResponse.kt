package com.mmetzner.vehiclemaintenance.feature.vehicle.data.remote

import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Maintenance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaintenanceResponse(
    @SerialName("id") val id: String,
    @SerialName("date") val date: String,
    @SerialName("description") val description: String,
    @SerialName("workshopName") val workshopName: String?,
    @SerialName("photoUrls") val photoUrls: List<String>? = null
)

fun MaintenanceResponse.toDomain() = Maintenance(
    id = this.id,
    date = this.date,
    description = this.description,
    workshopName = this.workshopName ?: "Não informada",
    photoUrls = this.photoUrls ?: emptyList()
)