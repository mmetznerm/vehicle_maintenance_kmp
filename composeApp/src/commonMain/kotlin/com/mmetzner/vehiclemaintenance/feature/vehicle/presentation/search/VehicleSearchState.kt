package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.search

import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.model.Vehicle

sealed interface VehicleSearchState {
    data object Idle : VehicleSearchState
    data object Loading : VehicleSearchState
    data class Success(val vehicle: Vehicle) : VehicleSearchState
    data class Error(val message: String) : VehicleSearchState
}


