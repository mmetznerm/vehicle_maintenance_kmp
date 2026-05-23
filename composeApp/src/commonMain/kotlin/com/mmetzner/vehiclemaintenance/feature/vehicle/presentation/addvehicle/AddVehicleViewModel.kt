package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.addvehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.model.Vehicle
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddVehicleViewModel(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<AddVehicleUiEvent>()
    val uiEvent: SharedFlow<AddVehicleUiEvent> = _uiEvent.asSharedFlow()

    fun saveVehicle(plate: String, model: String, brand: String, yearStr: String) {
        if (plate.isBlank() || model.isBlank() || brand.isBlank()) return
        val year = yearStr.toIntOrNull() ?: return

        viewModelScope.launch {
            val newVehicle = Vehicle(
                plate = plate.uppercase().trim(),
                model = model.trim(),
                brand = brand.trim(),
                year = year,
                maintenances = emptyList()
            )

            repository.addVehicle(newVehicle)

            _uiEvent.emit(AddVehicleUiEvent.NavigateBack)
        }
    }
}

sealed interface AddVehicleUiEvent {
    data object NavigateBack : AddVehicleUiEvent
}


