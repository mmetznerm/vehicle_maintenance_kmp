package com.mmetzner.vehiclemaintenance.feature.vehicle.data

import com.mmetzner.vehiclemaintenance.feature.vehicle.data.remote.VehicleResponse
import com.mmetzner.vehiclemaintenance.feature.vehicle.data.remote.toDomain
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.repository.VehicleRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class VehicleRepositoryImpl(
    private val httpClient: HttpClient
) : VehicleRepository {

    override suspend fun getVehicleByPlate(plate: String): Result<Vehicle> {
        return try {
            val response = httpClient.get("https://api.exemplo.com/v1/vehicles/$plate")
            
            if (response.status.isSuccess()) {
                val dto: VehicleResponse = response.body()
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception("Erro na requisição: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}