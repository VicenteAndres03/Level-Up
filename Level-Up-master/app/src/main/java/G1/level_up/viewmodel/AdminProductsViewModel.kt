package G1.level_up.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import G1.level_up.LevelUpApplication
import G1.level_up.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * `AdminProductsViewModel` es el ViewModel para la pantalla de administración de productos.
 * Se encarga de la lógica de negocio para obtener, añadir, actualizar y eliminar productos,
 * comunicándose con el `ProductoRepository`.
 *
 * @param application La instancia de la aplicación, utilizada para obtener la dependencia del repositorio.
 */
class AdminProductsViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia del repositorio de productos, obtenida a través de la clase Application.
    private val productoRepository = (application as LevelUpApplication).productoRepository

    // `_products` es el StateFlow mutable que contiene la lista de productos. Es privado.
    private val _products = MutableStateFlow<List<Producto>>(emptyList())
    // `products` es la versión inmutable del StateFlow, expuesta a la UI para ser observada.
    val products = _products.asStateFlow()

    // `_isLoading` controla la visibilidad del indicador de carga en la UI.
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        // Al crear el ViewModel, se inicia la carga de productos.
        refreshProducts()
    }

    /**
     * `refreshProducts` actualiza la lista de productos desde el repositorio.
     * Muestra un indicador de carga mientras se realiza la operación.
     */
    fun refreshProducts() {
        viewModelScope.launch {
            _isLoading.value = true // Inicia la carga.
            // La llamada al repositorio se hace en un hilo de fondo (IO).
            val updatedProducts = withContext(Dispatchers.IO) {
                productoRepository.obtenerProductos()
            }
            _products.value = updatedProducts // Actualiza la lista en el hilo principal.
            _isLoading.value = false // Finaliza la carga.
        }
    }

    /**
     * Añade un nuevo producto a la base de datos y refresca la lista.
     */
    fun addProduct(product: Producto) {
        viewModelScope.launch(Dispatchers.IO) { // Operación de BD en hilo de fondo.
            productoRepository.addProduct(product)
            // Se cambia al hilo principal para llamar a `refreshProducts`,
            // que a su vez actualizará la UI de forma segura.
            withContext(Dispatchers.Main) {
                refreshProducts()
            }
        }
    }

    /**
     * Actualiza un producto existente en la base de datos y refresca la lista.
     */
    fun updateProduct(product: Producto) {
        viewModelScope.launch(Dispatchers.IO) {
            productoRepository.updateProduct(product)
            withContext(Dispatchers.Main) {
                refreshProducts()
            }
        }
    }

    /**
     * Elimina un producto de la base de datos y refresca la lista.
     */
    fun deleteProduct(product: Producto) {
        viewModelScope.launch(Dispatchers.IO) {
            productoRepository.deleteProduct(product.id)
            withContext(Dispatchers.Main) {
                refreshProducts()
            }
        }
    }
}
