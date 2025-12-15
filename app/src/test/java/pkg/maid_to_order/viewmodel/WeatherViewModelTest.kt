package pkg.maid_to_order.viewmodel

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import pkg.maid_to_order.network.api.WeatherApi
import pkg.maid_to_order.network.api.WeatherClient
import pkg.maid_to_order.network.dto.CurrentWeatherDto
import pkg.maid_to_order.network.dto.WeatherResponse
import pkg.maid_to_order.util.MainDispatcherRule
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val api: WeatherApi = mockk()

    init {
        mockkObject(WeatherClient)
        every { WeatherClient.api } returns api
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `refreshWeather emits success state`() = runTest {
        coEvery {
            api.getCurrentWeather(any(), any(), any())
        } returns Response.success(
            WeatherResponse(
                latitude = 0.0,
                longitude = 0.0,
                currentWeather = CurrentWeatherDto(
                    temperature = 20.0,
                    windSpeed = 5.0,
                    windDirection = 0.0,
                    weatherCode = 0,
                    time = "2025-01-01T12:00"
                )
            )
        )

        val viewModel = WeatherViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is WeatherUiState.Success)
    }

    @Test
    fun `refreshWeather emits error on failure`() = runTest {
        coEvery { api.getCurrentWeather(any(), any(), any()) } throws RuntimeException("network")

        val viewModel = WeatherViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is WeatherUiState.Error)
    }
}
