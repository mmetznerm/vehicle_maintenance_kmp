package com.mmetzner.vehiclemaintenance.feature.vehicle.domain.repository

import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle

interface VehicleRepository {
    suspend fun getVehicleByPlate(plate: String): Result<Vehicle>
}