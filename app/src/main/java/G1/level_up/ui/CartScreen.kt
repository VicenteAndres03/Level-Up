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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(username: String) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val cartRepository = remember { CartRepository(context) }
    var cartItems by remember { mutableStateOf<Map<Producto, Int>>(emptyMap()) }
    var userId by remember { mutableStateOf<Long?>(null) }

    val total = cartItems.entries.sumOf { (producto, quantity) -> producto.precio * quantity }

    fun refreshCartItems(uid: Long) {
        cartItems = cartRepository.getCartItems(uid)
    }

    LaunchedEffect(username) {
        val user = userRepository.getUserByUsername(username)
        userId = user?.id
        userId?.let {
            refreshCartItems(it)
        }
    }

    Scaffold(
        containerColor = PrimaryDark,
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras", color = TextColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryDark)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (cartItems.isEmpty()) {
                // Mensaje de carrito vacío
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
                // Lista de productos en el carrito
                LazyColumn(
                    modifier = Modifier.weight(1f), // Ocupa el espacio disponible
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
                                    refreshCartItems(uid)
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

                // Sección de resumen de la compra
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryDark,
                    shadowElevation = 8.dp // Sombra para separar visualmente
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
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
                        Button(
                            onClick = {
                                userId?.let {
                                    cartRepository.clearCart(it)
                                    Toast.makeText(context, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                                    refreshCartItems(it)
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = DestructiveColor.copy(alpha = 0.8f))
                }
            }
        }
    }
}

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
