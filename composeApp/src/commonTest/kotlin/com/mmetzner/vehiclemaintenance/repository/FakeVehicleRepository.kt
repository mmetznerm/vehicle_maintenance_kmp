package com.mmetzner.vehiclemaintenance.repository

import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeOfflineFirstRepository : VehicleRepository {
    val databaseFlow = MutableStateFlow<Vehicle?>(null)
    var networkResult: Result<Unit> = Result.success(Unit)
    var syncCalled = false
    var addedVehicle: Vehicle? = null
    var syncOutboxCalled = false


    override suspend fun observeVehicle(plate: String): Flow<Vehicle?> {
        return databaseFlow
    }

    override suspend fun syncVehicle(plate: String): Result<Unit> {
        syncCalled = true
        return networkResult
    }

    override suspend fun addVehicle(vehicle: Vehicle) {
        addedVehicle = vehicle
    }

    override suspend fun syncPendingOutbox() {
        syncOutboxCalled = true
    }
}

