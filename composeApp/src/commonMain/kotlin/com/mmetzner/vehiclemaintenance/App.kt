package com.mmetzner.vehiclemaintenance

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mmetzner.vehiclemaintenance.core.navigation.AddVehicleRoute
import com.mmetzner.vehiclemaintenance.core.navigation.VehicleSearchRoute
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.VehicleSearchViewModel
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.add.AddVehicleScreen
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.add.AddVehicleViewModel
import com.seuprojeto.feature.vehicle.presentation.VehicleSearchScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = VehicleSearchRoute
        ) {
            composable<VehicleSearchRoute> {
                val viewModel = koinViewModel<VehicleSearchViewModel>()

                VehicleSearchScreen(
                    viewModel = viewModel,
                    onNavigateToAddVehicle = {
                        navController.navigate(AddVehicleRoute)
                    }
                )
            }

            composable<AddVehicleRoute> {
                val viewModel = koinViewModel<AddVehicleViewModel>()

                AddVehicleScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}