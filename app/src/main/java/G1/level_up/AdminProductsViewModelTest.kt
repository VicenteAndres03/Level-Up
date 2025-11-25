package G1.level_up

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import G1.level_up.model.Producto
import G1.level_up.repository.ProductoRepository
import G1.level_up.viewmodel.AdminProductsViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AdminProductsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockApplication = mockk<LevelUpApplication>(relaxed = true)
    private val mockRepository = mockk<ProductoRepository>()
    private lateinit var viewModel: AdminProductsViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val dummyProducts = listOf(
        Producto(1, "Mouse", "Gaming mouse", 50000, "Periféricos", 10, "url", "specs", "Provider A"),
        Producto(2, "Keyboard", "Mechanical keyboard", 90000, "Periféricos", 5, "url", "specs", "Provider B")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { mockApplication.productoRepository } returns mockRepository

        coEvery { mockRepository.obtenerProductos() } returns dummyProducts
        coEvery { mockRepository.addProduct(any()) } returns true
        coEvery { mockRepository.updateProduct(any()) } returns true
        coEvery { mockRepository.deleteProduct(any()) } returns true

        // Inyectar el dispatcher de pruebas en el ViewModel
        viewModel = AdminProductsViewModel(mockApplication as Application, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when ViewModel init, then products list is loaded correctly`() = runTest {
        advanceUntilIdle()

        assertEquals(dummyProducts, viewModel.products.value)
        coVerify(exactly = 1) { mockRepository.obtenerProductos() }
    }

    @Test
    fun `when addProduct is called successfully, then it refreshes the product list`() = runTest {
        val newProduct = Producto(0, "Monitor", "4K Monitor", 350000, "Monitores", 15, "url", "specs", "Provider C")
        val updatedList = dummyProducts + newProduct

        coEvery { mockRepository.obtenerProductos() } returns updatedList

        viewModel.addProduct(newProduct)
        advanceUntilIdle()

        coVerify(exactly = 1) { mockRepository.addProduct(newProduct) }
        coVerify(exactly = 2) { mockRepository.obtenerProductos() }
        assertEquals(updatedList, viewModel.products.value)
    }
}