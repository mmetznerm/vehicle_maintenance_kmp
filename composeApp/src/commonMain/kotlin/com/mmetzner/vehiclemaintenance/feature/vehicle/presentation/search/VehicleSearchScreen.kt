package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.model.Vehicle
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.search.VehicleSearchState
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.search.VehicleSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSearchScreen(
    viewModel: VehicleSearchViewModel,
    onNavigateToAddVehicle: () -> Unit,
    onNavigateToAddMaintenance: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    var plateQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consultar VeÃ­culo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddVehicle,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Cadastrar novo veÃ­culo"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ãrea de Busca
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = plateQuery,
                    onValueChange = { plateQuery = it.uppercase() }, // ForÃ§a maiÃºsculas
                    label = { Text("Digite a Placa") },
                    placeholder = { Text("ABC1D23") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            viewModel.searchVehicle(plateQuery)
                        }
                    )
                )

                Button(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.searchVehicle(plateQuery)
                    },
                    modifier = Modifier.height(56.dp) // Alinha com a altura do TextField
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gerenciamento de Estados (State Machine)
            when (val currentState = state) {
                is VehicleSearchState.Idle -> {
                    Text(
                        text = "Digite uma placa para buscar o histÃ³rico de manutenÃ§Ã£o.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }

                is VehicleSearchState.Loading -> {
                    CircularProgressIndicator()
                }

                is VehicleSearchState.Error -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentState.message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is VehicleSearchState.Success -> {
                    VehicleResultCard(
                        vehicle = currentState.vehicle,
                        onAddMaintenance = { onNavigateToAddMaintenance(currentState.vehicle.plate) }
                    )
                }
            }
        }
    }
}

/**
 * Componente privado para exibir os detalhes do veÃ­culo.
 * Isso mantÃ©m a Ã¡rvore principal limpa e legÃ­vel.
 */
@Composable
private fun VehicleResultCard(
    vehicle: Vehicle,
    onAddMaintenance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = vehicle.plate,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                if (vehicle.isPendingSync) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "SincronizaÃ§Ã£o pendente",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Pendente",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "${vehicle.brand} ${vehicle.model}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Ano: ${vehicle.year}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "HistÃ³rico de ManutenÃ§Ãµes",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            if (vehicle.maintenances.isNullOrEmpty()) {
                Text(
                    text = "Nenhum registro encontrado.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    vehicle.maintenances?.forEach { maintenance ->
                        MaintenanceItem(maintenance)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onAddMaintenance,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cadastrar ManutenÃ§Ã£o")
            }
        }
    }
}

@Composable
private fun MaintenanceItem(maintenance: com.mmetzner.vehiclemaintenance.feature.vehicle.domain.model.Maintenance) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = maintenance.date,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${maintenance.totalValue ?: 0.0} R$",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = maintenance.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "KM: ${maintenance.mileage ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = maintenance.workshopName ?: "Oficina nÃ£o informada",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



