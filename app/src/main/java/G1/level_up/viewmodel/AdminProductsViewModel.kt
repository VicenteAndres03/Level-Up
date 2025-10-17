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

class AdminProductsViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepository = (application as LevelUpApplication).productoRepository

    private val _products = MutableStateFlow<List<Producto>>(emptyList())
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            productoRepository.addProduct(product)
            withContext(Dispatchers.Main) {
                refreshProducts()
            }
        }
    }

    fun updateProduct(product: Producto) {
        viewModelScope.launch(Dispatchers.IO) {
            productoRepository.updateProduct(product)
            withContext(Dispatchers.Main) {
                refreshProducts()
            }
        }
    }

    fun deleteProduct(product: Producto) {
        viewModelScope.launch(Dispatchers.IO) {
            productoRepository.deleteProduct(product.id)
            withContext(Dispatchers.Main) {
                refreshProducts()
            }
        }
    }
}
