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
import pkg.maid_to_order.network.api.MaidToOrderApi
import pkg.maid_to_order.network.api.RetrofitClient
import pkg.maid_to_order.network.dto.DishDto
import pkg.maid_to_order.util.MainDispatcherRule
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val api: MaidToOrderApi = mockk()

    init {
        mockkObject(RetrofitClient)
        every { RetrofitClient.api } returns api
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loads dishes from api successfully`() = runTest {
        val dishesResponse = listOf(
            DishDto(
                id = 1,
                name = "Ramen",
                description = "Caldo y fideos",
                price = 8000.0,
                category = "Sopas",
                imageUrl = null,
                available = true
            )
        )
        coEvery { api.getDishes(any(), any(), any()) } returns Response.success(dishesResponse)
        coEvery { api.getSpecialDishes(any(), any()) } returns Response.success(emptyList())

        val viewModel = MenuViewModel()
        advanceUntilIdle()

        assert(viewModel.menuList.size == 1)
        assert(viewModel.menuList.first().name == "Ramen")
    }

    @Test
    fun `falls back to local menu when api fails`() = runTest {
        coEvery { api.getDishes(any(), any(), any()) } throws RuntimeException("network error")
        coEvery { api.getSpecialDishes(any(), any()) } returns Response.success(emptyList())

        val viewModel = MenuViewModel()
        advanceUntilIdle()

        // fallback list has 4 elementos
        assert(viewModel.menuList.size == 4)
        assert(viewModel.menuList.any { it.name == "Katsudon" })
    }
}
