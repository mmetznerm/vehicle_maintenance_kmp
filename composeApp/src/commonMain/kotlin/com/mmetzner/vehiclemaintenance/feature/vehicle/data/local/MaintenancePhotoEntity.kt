package com.mmetzner.vehiclemaintenance.feature.vehicle.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenance_photos",
    foreignKeys = [
        ForeignKey(
            entity = MaintenanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["maintenanceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("maintenanceId")]
)
data class MaintenancePhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val maintenanceId: String,
    val url: String 
)