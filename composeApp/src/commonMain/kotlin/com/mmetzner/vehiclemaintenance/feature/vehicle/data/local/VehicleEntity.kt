package com.mmetzner.vehiclemaintenance.feature.vehicle.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val plate: String,
    val model: String,
    val brand: String,
    val year: Int,
    @ColumnInfo(defaultValue = SyncStatus.SYNCED)
    val syncStatus: String = SyncStatus.SYNCED
)
