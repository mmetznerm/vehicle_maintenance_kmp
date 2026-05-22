package com.mmetzner.vehiclemaintenance.feature.vehicle.data

import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.SyncStatus
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.local.dao.VehicleDao
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.mapper.toDomain
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.mapper.toEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.mapper.toPendingEntity
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.mapper.toPhotoEntities
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.mapper.toRequestDto
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.remote.VehicleResponse
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.repository.VehicleRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class VehicleRepositoryImpl(
    private val httpClient: HttpClient,
    private val vehicleDao: VehicleDao
) : VehicleRepository {

    private val syncScope = CoroutineScope(Dispatchers.IO)

    override suspend fun observeVehicle(plate: String): Flow<Vehicle?> {
        return vehicleDao.observeVehicleByPlate(plate).map { relation ->
            relation?.toDomain()
        }
    }

    override suspend fun syncVehicle(plate: String): Result<Unit> {
        return try {
            val response = httpClient.get("https://api.exemplo_fake.com/v1/vehicles/$plate")

            if (response.status.isSuccess()) {
                val dto: VehicleResponse = response.body()

                val vehicleEntity = dto.toEntity()
                val maintenanceEntities = dto.maintenances?.map { it.toEntity(dto.plate) } ?: emptyList()
                val photoEntities = dto.maintenances?.flatMap { it.toPhotoEntities(it.id) } ?: emptyList()

                vehicleDao.syncVehicleData(vehicleEntity, maintenanceEntities, photoEntities)

                Result.success(Unit)
            } else {
                Result.failure(Exception("Fail on API: Code ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addVehicle(vehicle: Vehicle) {
        val entity = vehicle.toPendingEntity()
        vehicleDao.insertVehicle(entity)

        syncScope.launch {
            syncPendingOutbox()
        }
    }

    override suspend fun syncPendingOutbox() {
        try {
            val pendingVehicles = vehicleDao.getVehiclesByStatus(SyncStatus.PENDING)

            for (entity in pendingVehicles) {
                val response = httpClient.post("https://api.exemplo_fake.com/v1/vehicles") {
                    contentType(ContentType.Application.Json)
                    setBody(entity.toRequestDto())
                }

                if (response.status.isSuccess()) {
                    vehicleDao.updateVehicleSyncStatus(entity.plate, SyncStatus.SYNCED)
                }
            }
        } catch (_: Exception) {

        }
    }
}