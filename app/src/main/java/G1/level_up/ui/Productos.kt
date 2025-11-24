package G1.level_up.ui


import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.model.User
import G1.level_up.repository.CartRepository
import G1.level_up.repository.UserRepository
import G1.level_up.viewmodel.HomeViewModel
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

/**
 * `ProductsScreen` muestra una lista de todos los productos disponibles en la tienda.
 * Los usuarios pueden ver los productos y, si han iniciado sesión, añadirlos a su carrito.
 *
 * @param modifier Modificador de Compose para personalizar el diseño.
 * @param username El nombre del usuario que ha iniciado sesión, o `null` si nadie ha iniciado sesión.
 */
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    username: String?
) {
    val context = LocalContext.current
    // Se utiliza una Factory para poder pasar el contexto de Application al HomeViewModel.
    val factory = HomeViewModelFactory(context.applicationContext as Application)
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val productos by viewModel.productos.collectAsState() // Se observa la lista de productos del ViewModel.
    val userRepository = remember { UserRepository(context) }
    val cartRepository = remember { CartRepository(context) }
    var user by remember { mutableStateOf<User?>(null) }

    // `LaunchedEffect` se usa para buscar los datos del usuario cuando el `username` cambia.
    LaunchedEffect(username) {
        user = if (username != null) {
            userRepository.getUserByUsername(username)
        } else {
            null
        }
    }

    // `LazyColumn` es una lista perezosa que solo renderiza los elementos visibles en pantalla.
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A1931)), // Fondo oscuro de la app
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(productos) { producto ->
            ProductItem(
                producto = producto,
                onAddToCartClick = {
                    // Solo se puede añadir al carrito si hay un usuario logueado.
                    user?.id?.let { userId ->
                        val itemsAdded = cartRepository.addToCart(userId, producto.id)
                        if (itemsAdded != -1L) {
                            Toast.makeText(context, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No hay más stock para ${producto.nombre}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                isLoggedIn = user != null // Se pasa el estado de login al item.
            )
        }
    }
}

/**
 * `ProductItem` es un Composable que representa la tarjeta de un solo producto en la lista.
 *
 * @param producto El objeto `Producto` a mostrar.
 * @param onAddToCartClick Lambda que se ejecuta al pulsar el botón de "Añadir al carrito".
 * @param isLoggedIn Booleano que indica si el usuario ha iniciado sesión.
 */
@Composable
fun ProductItem(
    producto: Producto,
    onAddToCartClick: () -> Unit,
    isLoggedIn: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2942))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // `AsyncImage` (de la librería Coil) carga la imagen del producto desde una URL.
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.nombre,
                placeholder = painterResource(id = R.drawable.logolevelup),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(producto.categoria, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$${producto.precio}", color = Color(0xFFFF8C00), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

                // El botón "Añadir al carrito" solo se muestra si el usuario está logueado.
                if (isLoggedIn) {
                    Spacer(modifier = Modifier.height(8.dp))
                    val isOutOfStock = producto.stock <= 0
                    Button(
                        onClick = onAddToCartClick,
                        enabled = !isOutOfStock, // Se deshabilita el botón si el stock es 0 o menos.
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF8C00),
                            disabledContainerColor = Color.Gray // Color cuando el botón está deshabilitado.
                        ),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        // El texto del botón cambia si el producto está agotado.
                        Text(
                            text = if (isOutOfStock) "Agotado" else "Añadir al carrito",
                            color = if (isOutOfStock) Color.White else Color(0xFF0A1931),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * `HomeViewModelFactory` es una clase factory que permite crear una instancia de `HomeViewModel`
 * pasándole dependencias (en este caso, `Application`) a su constructor.
 * Esto es necesario porque los ViewModels con parámetros no pueden ser instanciados directamente por el sistema.
 */
class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}
