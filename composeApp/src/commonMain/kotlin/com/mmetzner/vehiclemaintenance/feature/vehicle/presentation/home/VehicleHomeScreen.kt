package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.model.Maintenance
import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.model.Vehicle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleHomeScreen(
    viewModel: VehicleHomeViewModel,
    onRegisterVehicle: () -> Unit,
    onAddMaintenance: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Meu veículo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sair"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state) {
                VehicleHomeState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                VehicleHomeState.Empty -> {
                    EmptyHomeState(
                        onRegisterVehicle = onRegisterVehicle,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is VehicleHomeState.Error -> {
                    ErrorState(
                        message = currentState.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is VehicleHomeState.Content -> {
                    HomeContent(
                        vehicle = currentState.vehicle,
                        onAddMaintenance = { onAddMaintenance(currentState.vehicle.plate) },
                        onShareHistory = {
                            uriHandler.openUri(currentState.vehicle.toWhatsAppShareUrl())
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

private fun Vehicle.toWhatsAppShareUrl(): String {
    val maintenanceCount = maintenances?.size ?: 0
    val message = buildString {
        appendLine("Histórico de manutenção")
        appendLine("$brand $model ($year)")
        appendLine("Placa: $plate")
        append("Manutenções registradas: $maintenanceCount")
    }

    return "https://wa.me/?text=${message.urlEncoded()}"
}

private fun String.urlEncoded(): String = buildString {
    this@urlEncoded.encodeToByteArray().forEach { byte ->
        val value = byte.toInt() and 0xFF
        val char = value.toChar()
        when {
            char in 'A'..'Z' || char in 'a'..'z' || char in '0'..'9' -> append(char)
            char == '-' || char == '_' || char == '.' || char == '~' -> append(char)
            char == ' ' -> append("%20")
            else -> {
                append('%')
                append(value.toString(16).uppercase().padStart(2, '0'))
            }
        }
    }
}

@Composable
private fun EmptyHomeState(
    onRegisterVehicle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }

        Text(
            text = "Cadastre seu veículo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Depois do cadastro, o histórico fica privado na sua conta e pode ser compartilhado somente quando você quiser.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 360.dp)
        )

        Button(onClick = onRegisterVehicle) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Cadastrar veículo")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeContent(
    vehicle: Vehicle,
    onAddMaintenance: () -> Unit,
    onShareHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PlateBadge(vehicle.plate)

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "${vehicle.brand} ${vehicle.model}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Histórico privado do proprietário",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f)
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VehicleInfoChip(Icons.Default.CalendarMonth, vehicle.year.toString())
                    VehicleInfoChip(Icons.Default.LocalGasStation, "Flex")
                    VehicleInfoChip(Icons.Default.Speed, "Local-first")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onAddMaintenance,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nova")
            }

            FilledTonalButton(
                onClick = onShareHistory,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.IosShare, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Compartilhar")
            }
        }

        Text(
            text = "Manutenções",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        if (vehicle.maintenances.isNullOrEmpty()) {
            EmptyMaintenanceState()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                vehicle.maintenances.forEach { maintenance ->
                    MaintenanceItem(maintenance)
                }
            }
        }
    }
}

@Composable
private fun EmptyMaintenanceState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text(
            text = "Nenhuma manutenção registrada ainda.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun PlateBadge(plate: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF111417),
        contentColor = Color(0xFFFFD84D)
    ) {
        Text(
            text = plate,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun VehicleInfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    AssistChip(
        onClick = {},
        label = { Text(label) },
        leadingIcon = {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = null
    )
}

@Composable
private fun MaintenanceItem(maintenance: Maintenance) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = maintenance.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "R$ ${maintenance.totalValue ?: 0.0}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "${maintenance.date}  |  ${maintenance.mileage ?: "--"} km",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            Text(
                text = maintenance.workshopName ?: "Oficina não informada",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
