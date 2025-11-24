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

/**
 * `HomeViewModel` es el ViewModel para la pantalla principal (Home).
 * Extiende `AndroidViewModel` para poder acceder al contexto de la aplicación y, a través de él,
 * al repositorio de productos.
 *
 * Su responsabilidad es obtener la lista de productos del repositorio y exponerla a la UI
 * de una manera que sea segura y reactiva a los cambios.
 *
 * @param application La instancia de la aplicación, utilizada para obtener dependencias como el repositorio.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    // Se obtiene la instancia del repositorio de productos desde la clase `Application`.
    // Este es un método simple de inyección de dependencias.
    private val repository: ProductoRepository = (application as LevelUpApplication).productoRepository

    // `_productos` es un `MutableStateFlow` que contiene la lista actual de productos.
    // Es privado para que solo el ViewModel pueda modificarlo.
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())

    // `productos` es un `StateFlow` inmutable expuesto a la UI.
    // La UI observa este flujo para recibir actualizaciones automáticamente cuando la lista cambia.
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // El bloque `init` se ejecuta cuando se crea una instancia del ViewModel.
    init {
        cargarProductos() // Llama a la función para cargar los productos desde el repositorio.
    }

    /**
     * `cargarProductos` obtiene la lista de productos del repositorio de forma asíncrona.
     * Utiliza `viewModelScope` para lanzar una corrutina que se cancelará automáticamente
     * cuando el ViewModel sea destruido, evitando fugas de memoria.
     */
    private fun cargarProductos() {
        // Se lanza la corrutina en el despachador de IO (Input/Output),
        // ideal para operaciones de base de datos o de red que pueden tardar.
        viewModelScope.launch(Dispatchers.IO) {
            val lista = repository.obtenerProductos() // Llama al método del repositorio para get la data.

            // Una vez obtenidos los datos, se cambia al despachador Principal (Main)
            // para actualizar el `StateFlow`. Las actualizaciones de la UI siempre deben
            // ocurrir en el hilo principal.
            withContext(Dispatchers.Main) {
                _productos.value = lista // Actualiza el valor, lo que notificará a la UI.
            }
        }
    }
}
