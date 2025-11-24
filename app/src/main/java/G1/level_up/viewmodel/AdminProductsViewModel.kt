// Ruta: app/src/main/java/G1/level_up/viewmodel/AdminProductsViewModel.kt

package G1.level_up.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import G1.level_up.model.Producto
import G1.level_up.repository.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminProductsViewModel(application: Application) : AndroidViewModel(application) {

    // Se asume que ProductoRepository ahora usa la API remota
    private val productoRepository = ProductoRepository(application)

    private val _products = MutableStateFlow<List<Producto>>(emptyList())
    val products: StateFlow<List<Producto>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        refreshProducts()
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            // Llamada al repositorio (API remota o fallback local)
            val updatedProducts = withContext(Dispatchers.IO) {
                productoRepository.obtenerProductos()
            }
            _products.value = updatedProducts
            _isLoading.value = false
        }
    }

    fun addProduct(product: Producto) {
        viewModelScope.launch {
            // [CORRECCIÓN] Ejecutamos la operación en IO y capturamos el resultado Boolean.
            val success = withContext(Dispatchers.IO) {
                productoRepository.addProduct(product)
            }
            // Solo recargamos si la operación remota fue exitosa.
            if (success) {
                refreshProducts()
            }
            // Opcional: Si 'success' es false, podrías exponer un error a la UI.
        }
    }

    fun updateProduct(product: Producto) {
        viewModelScope.launch {
            // [CORRECCIÓN] Ejecutamos la operación en IO y capturamos el resultado Boolean.
            val success = withContext(Dispatchers.IO) {
                productoRepository.updateProduct(product)
            }
            // Solo recargamos si la operación remota fue exitosa.
            if (success) {
                refreshProducts()
            }
        }
    }

    fun deleteProduct(product: Producto) {
        viewModelScope.launch {
            // [CORRECCIÓN] Ejecutamos la operación en IO y capturamos el resultado Boolean.
            val success = withContext(Dispatchers.IO) {
                productoRepository.deleteProduct(product.id)
            }
            // Solo recargamos si la operación remota fue exitosa.
            if (success) {
                refreshProducts()
            }
        }
    }
}