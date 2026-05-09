package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation

import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.repository.VehicleRepository

class FakeVehicleRepository : VehicleRepository {
    var resultToReturn: Result<Vehicle> = Result.failure(Exception("Não configurado"))

    override suspend fun getVehicleByPlate(plate: String): Result<Vehicle> {
        return resultToReturn
    }
}

