package com.seuprojeto.feature.vehicle.presentation

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
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.VehicleSearchState
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.VehicleSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSearchScreen(
    viewModel: VehicleSearchViewModel,
    onNavigateToAddVehicle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    var plateQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consultar Veículo") },
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
                    contentDescription = "Cadastrar novo veículo"
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
            // Área de Busca
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = plateQuery,
                    onValueChange = { plateQuery = it.uppercase() }, // Força maiúsculas
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
                        text = "Digite uma placa para buscar o histórico de manutenção.",
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
                    VehicleResultCard(vehicle = currentState.vehicle)
                }
            }
        }
    }
}

/**
 * Componente privado para exibir os detalhes do veículo.
 * Isso mantém a árvore principal limpa e legível.
 */
@Composable
private fun VehicleResultCard(
    vehicle: Vehicle,
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

                // O Ícone da Mágica do Offline-First
                if (vehicle.isPendingSync) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Sincronização pendente",
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

            // Espaço para mostrar as manutenções no futuro...
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Histórico: ${vehicle.maintenances?.size} registro(s)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}