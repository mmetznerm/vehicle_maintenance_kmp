package com.mmetzner.vehiclemaintenance.core.database

import androidx.room.*
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.MaintenanceEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.MaintenancePhotoEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.VehicleEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.dao.VehicleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [VehicleEntity::class, MaintenanceEntity::class, MaintenancePhotoEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

expect class DatabaseBuilder {
    fun createBuilder(): RoomDatabase.Builder<AppDatabase>
}

fun createRoomDatabase(builder: DatabaseBuilder): AppDatabase {
    return builder.createBuilder()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
