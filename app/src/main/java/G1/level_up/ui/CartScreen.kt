package G1.level_up.ui

import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.repository.CartRepository
import G1.level_up.repository.UserRepository
import G1.level_up.ui.theme.*
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * `CartScreen` es la pantalla que muestra al usuario los productos que ha añadido a su carrito de compras.
 * Permite ver la lista de productos, modificar las cantidades y finalizar la compra.
 *
 * @param username El nombre de usuario del usuario actual, necesario para obtener su ID y su carrito.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(username: String) {
    // Se obtiene el contexto actual, necesario para los repositorios y Toasts.
    val context = LocalContext.current
    // Se instancian los repositorios para acceder a la base de datos.
    val userRepository = remember { UserRepository(context) }
    val cartRepository = remember { CartRepository(context) }
    // Estado para almacenar los productos y sus cantidades en el carrito.
    var cartItems by remember { mutableStateOf<Map<Producto, Int>>(emptyMap()) }
    // Estado para almacenar el ID del usuario.
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

    // `Scaffold` proporciona la estructura básica de la pantalla (barra superior, contenido, etc.).
    Scaffold(
        containerColor = PrimaryDark,
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras", color = TextColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryDark)
            )
        }
    ) { innerPadding ->
        // Columna principal que organiza el contenido de la pantalla.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Comprueba si el carrito está vacío.
            if (cartItems.isEmpty()) {
                // Si está vacío, muestra un mensaje centrado.
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito vacío",
                            modifier = Modifier.size(80.dp),
                            tint = TextColor.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tu carrito está vacío",
                            color = TextColor.copy(alpha = 0.7f),
                            fontSize = 18.sp
                        )
                    }
                }
            } else {
                // Si no está vacío, muestra la lista de productos.
                LazyColumn(
                    modifier = Modifier.weight(1f), // Ocupa todo el espacio vertical disponible, dejando sitio para el resumen.
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems.entries.toList()) { (producto, quantity) ->
                        CartItem(
                            producto = producto,
                            quantity = quantity,
                            onIncrease = {
                                userId?.let { uid ->
                                    cartRepository.addToCart(uid, producto.id)
                                    refreshCartItems(uid) // Recarga el carrito para mostrar la nueva cantidad.
                                }
                            },
                            onDecrease = {
                                userId?.let { uid ->
                                    cartRepository.removeFromCart(uid, producto.id)
                                    refreshCartItems(uid)
                                }
                            },
                            onRemove = {
                                userId?.let { uid ->
                                    cartRepository.removeAllUnitsOfProductFromCart(uid, producto.id)
                                    refreshCartItems(uid)
                                }
                            }
                        )
                    }
                }

                // Sección fija en la parte inferior que muestra el resumen de la compra.
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryDark,
                    shadowElevation = 8.dp // Añade una sombra para destacar la sección.
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Fila para mostrar el texto "Total:" y el importe.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total:", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextColor)
                            Text(
                                "$${total}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Botón para finalizar la compra.
                        Button(
                            onClick = {
                                userId?.let {
                                    cartRepository.clearCart(it) // Limpia el carrito en la base de datos.
                                    Toast.makeText(context, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                                    refreshCartItems(it) // Recarga el carrito para que aparezca vacío.
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent)
                        ) {
                            Text(
                                "Finalizar Compra",
                                fontWeight = FontWeight.Bold,
                                color = PrimaryDark,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * `CartItem` es el Composable que representa un solo artículo en la lista del carrito.
 *
 * @param producto El objeto `Producto` a mostrar.
 * @param quantity La cantidad de este producto en el carrito.
 * @param onIncrease Lambda que se ejecuta para aumentar la cantidad.
 * @param onDecrease Lambda que se ejecuta para disminuir la cantidad.
 * @param onRemove Lambda que se ejecuta para eliminar todas las unidades del producto.
 */
@Composable
fun CartItem(
    producto: Producto,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto, cargada de forma asíncrona.
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.nombre,
                placeholder = painterResource(id = R.drawable.logolevelup),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Columna con el nombre y el precio del producto.
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    color = TextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${producto.precio}",
                    color = PrimaryAccent,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Columna con los controles para modificar la cantidad.
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Botones para disminuir y aumentar la cantidad.
                    SmallIconButton(onClick = onDecrease, icon = Icons.Default.Remove)
                    Text(
                        text = quantity.toString(),
                        color = TextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    SmallIconButton(onClick = onIncrease, icon = Icons.Default.Add)
                }
                // Botón para eliminar completamente el producto del carrito.
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = DestructiveColor.copy(alpha = 0.8f))
                }
            }
        }
    }
}

/**
 * `SmallIconButton` es un botón pequeño y reutilizable con un ícono, usado para los controles de cantidad.
 *
 * @param onClick La acción a ejecutar cuando se pulsa el botón.
 * @param icon El `ImageVector` (ícono) a mostrar en el botón.
 */
@Composable
fun SmallIconButton(onClick: () -> Unit, icon: ImageVector) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(32.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent.copy(alpha = 0.2f))
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ButtonAccent, modifier = Modifier.size(20.dp))
    }
}
