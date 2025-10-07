package G1.level_up.viewmodel

import G1.level_up.model.Producto
import G1.level_up.repository.ProductoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(){
    private val repository = ProductoRepository()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())

    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos(){
        viewModelScope.launch {
            val lista = repository.obtenerProductos()

            _productos.value = lista
        }
    }
}