// Ruta: app/src/main/java/G1/level_up/viewmodel/HomeViewModel.kt

package G1.level_up.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import G1.level_up.LevelUpApplication
import G1.level_up.model.Producto
import G1.level_up.repository.ProductoRepository
import androidx.lifecycle.viewModelScope
import G1.level_up.network.ExternalApiRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductoRepository = (application as LevelUpApplication).productoRepository
    private val cryptoApiService = ExternalApiRetrofitClient.cryptoService // Nueva dependencia

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // Nuevo StateFlow para el precio de la API externa
    private val _bitcoinPrice = MutableStateFlow<String>("Cargando...")
    val bitcoinPrice: StateFlow<String> = _bitcoinPrice.asStateFlow() // IL3.1 Consumo de API externa

    init {
        cargarProductos()
        cargarPrecioBitcoin() // Llamada para obtener datos de la API externa
    }

    private fun cargarProductos() {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = repository.obtenerProductos()
            withContext(Dispatchers.Main) {
                _productos.value = lista
            }
        }
    }

    // Nuevo método para consumir la API externa
    private fun cargarPrecioBitcoin() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = cryptoApiService.getCurrentBitcoinPrice()
                if (response.isSuccessful && response.body() != null) {
                    val price = response.body()!!.bpi.usd.rateFloat
                    // Formatear el precio
                    val formattedPrice = String.format(Locale.US, "US$%,.2f", price)
                    withContext(Dispatchers.Main) {
                        _bitcoinPrice.value = "BTC: $formattedPrice"
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _bitcoinPrice.value = "BTC: Error"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _bitcoinPrice.value = "BTC: Offline"
                }
            }
        }
    }
}