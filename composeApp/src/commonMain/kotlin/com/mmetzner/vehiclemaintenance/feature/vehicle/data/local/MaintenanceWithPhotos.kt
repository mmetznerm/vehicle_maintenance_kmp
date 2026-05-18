package com.mmetzner.vehiclemaintenance.feature.vehicle.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class MaintenanceWithPhotos(
    @Embedded val maintenance: MaintenanceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "maintenanceId"
    )
    val photos: List<MaintenancePhotoEntity>
)