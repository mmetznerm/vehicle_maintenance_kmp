package com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class VehicleWithMaintenances(
    @Embedded val vehicle: VehicleEntity,
    @Relation(
        entity = MaintenanceEntity::class,
        parentColumn = "plate",
        entityColumn = "vehiclePlate"
    )
    val maintenances: List<MaintenanceWithPhotos>
)


