package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.home

import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.model.Vehicle

sealed interface VehicleHomeState {
    data object Loading : VehicleHomeState
    data object Empty : VehicleHomeState
    data class Content(val vehicle: Vehicle) : VehicleHomeState
    data class Error(val message: String) : VehicleHomeState
}
