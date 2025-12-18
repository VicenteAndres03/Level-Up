package G1.level_up.viewmodel

import G1.level_up.model.Producto
import G1.level_up.repository.ProductoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val productoRepository: ProductoRepository) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    init {
        fetchProductos() // Carga automáticamente al abrir la App
    }

    fun fetchProductos() {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = productoRepository.obtenerProductos()
            _productos.value = lista
        }
    }
}