package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VehicleSearchViewModel(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _state = MutableStateFlow<VehicleSearchState>(VehicleSearchState.Idle)
    val state: StateFlow<VehicleSearchState> = _state.asStateFlow()

    fun searchVehicle(plate: String) {
        if (plate.isBlank()) return
        
        _state.value = VehicleSearchState.Loading
        
        viewModelScope.launch {
            repository.getVehicleByPlate(plate).fold(
                onSuccess = { vehicle ->
                    _state.value = VehicleSearchState.Success(vehicle)
                },
                onFailure = { exception ->
                    _state.value = VehicleSearchState.Error(exception.message ?: "Erro desconhecido")
                }
            )
        }
    }
}