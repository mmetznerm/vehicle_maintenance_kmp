package com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.dao

import androidx.room.*
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.MaintenanceEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.MaintenancePhotoEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.VehicleEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.VehicleWithMaintenances
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Transaction
    @Query("SELECT * FROM vehicles WHERE plate = :plate")
    fun observeVehicleByPlate(plate: String): Flow<VehicleWithMaintenances?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenances(maintenances: List<MaintenanceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<MaintenancePhotoEntity>)

    /**
     * Sincronização Atômica: Se a inserção de fotos falhar, 
     * nada é salvo, mantendo o estado local íntegro.
     */
    @Transaction
    suspend fun syncVehicleData(
        vehicle: VehicleEntity,
        maintenances: List<MaintenanceEntity>,
        photos: List<MaintenancePhotoEntity>
    ) {
        insertVehicle(vehicle)
        insertMaintenances(maintenances)
        insertPhotos(photos)
    }
}