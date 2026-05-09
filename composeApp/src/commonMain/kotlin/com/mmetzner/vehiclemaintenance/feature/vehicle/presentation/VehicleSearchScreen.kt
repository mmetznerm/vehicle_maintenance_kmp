package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vehiclemaintenance.composeapp.generated.resources.Res
import vehiclemaintenance.composeapp.generated.resources.button_search
import vehiclemaintenance.composeapp.generated.resources.maintenance_history
import vehiclemaintenance.composeapp.generated.resources.search_plate_label
import vehiclemaintenance.composeapp.generated.resources.search_plate_label_warning

@Composable
fun VehicleSearchScreen(
    onNavigateToDetails: (String) -> Unit,
    viewModel: VehicleSearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var plateInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.maintenance_history),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = plateInput,
            onValueChange = { plateInput = it.uppercase() },
            label = { Text(stringResource(Res.string.search_plate_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = { viewModel.searchVehicle(plateInput) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            enabled = plateInput.isNotBlank() && state !is VehicleSearchState.Loading
        ) {
            Text(stringResource(Res.string.button_search))
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val currentState = state) {
            is VehicleSearchState.Loading -> CircularProgressIndicator()
            is VehicleSearchState.Error -> ErrorState(currentState.message)
            is VehicleSearchState.Success -> {
                VehicleSummaryCard(
                    vehicle = currentState.vehicle,
                    onClick = { onNavigateToDetails(currentState.vehicle.plate) }
                )
            }
            is VehicleSearchState.Idle -> {
                Text(stringResource(Res.string.search_plate_label_warning))
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Text(text = message, color = MaterialTheme.colorScheme.error)
}

@Composable
fun VehicleSummaryCard(vehicle: Vehicle, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${vehicle.brand} ${vehicle.model}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Placa: ${vehicle.plate}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Ver histórico completo >", 
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}