package com.mmetzner.vehiclemaintenance

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mmetzner.vehiclemaintenance.core.navigation.VehicleDetailsRoute
import com.mmetzner.vehiclemaintenance.core.navigation.VehicleSearchRoute
import com.mmetzner.vehiclemaintenance.feature.vehicle.presentation.VehicleSearchScreen

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
                VehicleSearchScreen(
                    onNavigateToDetails = { plate ->
                        navController.navigate(VehicleDetailsRoute(plate))
                    }
                )
            }

            composable<VehicleDetailsRoute> { backStackEntry ->
                val route: VehicleDetailsRoute = backStackEntry.toRoute()
            }
        }
    }
}