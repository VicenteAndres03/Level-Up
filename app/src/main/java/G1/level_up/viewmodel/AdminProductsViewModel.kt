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
            val updatedProducts = withContext(Dispatchers.IO) {
                productoRepository.obtenerProductos()
            }
            _products.value = updatedProducts
            _isLoading.value = false
        }
    }

    fun addProduct(product: Producto) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                productoRepository.addProduct(product)
            }
            if (success) {
                refreshProducts()
            }
        }
    }

    fun updateProduct(product: Producto) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                productoRepository.updateProduct(product)
            }
            if (success) {
                refreshProducts()
            }
        }
    }

    fun deleteProduct(product: Producto) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                productoRepository.deleteProduct(product.id)
            }
            if (success) {
                refreshProducts()
            }
        }
    }
}