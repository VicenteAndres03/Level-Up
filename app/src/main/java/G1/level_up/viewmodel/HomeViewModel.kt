package G1.level_up.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import G1.level_up.LevelUpApplication
import G1.level_up.model.Producto
import G1.level_up.repository.ProductoRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductoRepository = (application as LevelUpApplication).productoRepository

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = repository.obtenerProductos()
            withContext(Dispatchers.Main) {
                _productos.value = lista
            }
        }
    }
}
