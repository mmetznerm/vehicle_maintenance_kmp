package com.mmetzner.vehiclemaintenance.feature.vehicle.presentation

import com.mmetzner.vehiclemaintenance.feature.vehicle.domain.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleSearchViewModelTest {

    private lateinit var repository: FakeVehicleRepository
    private lateinit var viewModel: VehicleSearchViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeVehicleRepository()
        viewModel = VehicleSearchViewModel(repository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when the search is triggered, the state should transition from Idle to Loading and then Success`() = runTest {
        val mockVehicle = Vehicle(
            plate = "ABC1234",
            model = "Civic",
            brand = "Honda",
            year = 2022,
            maintenances = emptyList()
        )
        repository.resultToReturn = Result.success(mockVehicle)

        assertEquals(VehicleSearchState.Idle, viewModel.state.value, "O estado inicial deve ser Idle")
        
        viewModel.searchVehicle("ABC1234")

        assertEquals(VehicleSearchState.Loading, viewModel.state.value, "O estado deve mudar para Loading imediatamente")
        
        advanceUntilIdle()

        val finalState = viewModel.state.value
        assertTrue(finalState is VehicleSearchState.Success, "O estado final deve ser Success")
        assertEquals("ABC1234", finalState.vehicle.plate)
    }

    @Test
    fun `when the search fails, the state should transition to Error`() = runTest {
        val errorMessage = "Veículo não encontrado"
        repository.resultToReturn = Result.failure(Exception(errorMessage))

        viewModel.searchVehicle("XYZ9999")
        advanceUntilIdle()

        val finalState = viewModel.state.value
        assertTrue(finalState is VehicleSearchState.Error)
        assertEquals(errorMessage, finalState.message)
    }
}