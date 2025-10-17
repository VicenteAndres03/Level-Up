package G1.level_up.ui

import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.repository.CartRepository
import G1.level_up.repository.UserRepository
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
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

/**
 * `CartScreen` es la pantalla que muestra al usuario los productos que ha añadido a su carrito de compras.
 * Permite aumentar o disminuir la cantidad de cada producto y finalizar la compra.
 *
 * @param username El nombre de usuario del usuario actual, necesario para obtener su ID y su carrito.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(username: String) {
    val context = LocalContext.current
    // Se instancian los repositorios para acceder a la base de datos.
    val userRepository = remember { UserRepository(context) }
    val cartRepository = remember { CartRepository(context) }
    // Estado para almacenar los productos y sus cantidades en el carrito.
    var cartItems by remember { mutableStateOf<Map<Producto, Int>>(emptyMap()) }
    var userId by remember { mutableStateOf<Long?>(null) }

    // Calcula el precio total del carrito sumando el precio de cada producto por su cantidad.
    val total = cartItems.entries.sumOf { (producto, quantity) -> producto.precio * quantity }

    // Función para recargar los productos del carrito desde la base de datos.
    fun refreshCartItems(uid: Long) {
        cartItems = cartRepository.getCartItems(uid)
    }

    // `LaunchedEffect` se usa para obtener el ID del usuario de forma asíncrona cuando el `username` cambia.
    LaunchedEffect(username) {
        val user = userRepository.getUserByUsername(username)
        userId = user?.id
        userId?.let {
            refreshCartItems(it) // Carga los productos del carrito una vez que se tiene el ID.
        }
    }

    Scaffold(
        containerColor = Color(0xFF0A1931),
        bottomBar = { // Barra inferior que solo aparece si el carrito no está vacío.
            if (cartItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Total: $$total", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            userId?.let {
                                cartRepository.clearCart(it) // Vacía el carrito en la BD.
                                Toast.makeText(context, "Compra realizada", Toast.LENGTH_SHORT).show() // Muestra un mensaje.
                                refreshCartItems(it) // Refresca la UI para mostrar el carrito vacío.
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00))
                    ) {
                        Text("Finalizar Compra", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text("Carrito de Compras", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            if (cartItems.isEmpty()) {
                // Muestra un mensaje si el carrito está vacío.
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tu carrito está vacío", color = Color.White.copy(alpha = 0.7f), fontSize = 18.sp)
                }
            } else {
                // Muestra la lista de productos en el carrito.
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(cartItems.entries.toList()) { (producto, quantity) ->
                        CartItem(
                            producto = producto,
                            quantity = quantity,
                            onIncrease = { userId?.let { cartRepository.addToCart(it, producto.id); refreshCartItems(it) } },
                            onDecrease = { userId?.let { cartRepository.removeFromCart(it, producto.id); refreshCartItems(it) } }
                        )
                    }
                }
            }
        }
    }
}

/**
 * `CartItem` es el Composable para un solo artículo en el carrito.
 */
@Composable
fun CartItem(producto: Producto, quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Card(/* ... */) {
        Row(/* ... */) {
            // ... (Imagen, Nombre, Precio)
            // Muestra los botones para aumentar o disminuir la cantidad.
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, enabled = quantity > 0) { /* ... */ }
                Text(text = quantity.toString(), /* ... */)
                IconButton(onClick = onIncrease) { /* ... */ }
            }
        }
    }
}
