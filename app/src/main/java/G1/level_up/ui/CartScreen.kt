package G1.level_up.ui

import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.repository.CartRepository
import G1.level_up.repository.UserRepository
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun CartScreen(username: String) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val cartRepository = remember { CartRepository(context) }
    var cartItems by remember { mutableStateOf<Map<Producto, Int>>(emptyMap()) }
    var userId by remember { mutableStateOf<Long?>(null) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1931))
            .padding(16.dp)
    ) {
        Text("Carrito de Compras", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(cartItems.entries.toList()) { (producto, quantity) ->
                CartItem(
                    producto = producto,
                    quantity = quantity,
                    onIncrease = {
                        userId?.let {
                            cartRepository.addToCart(it, producto.id)
                            refreshCartItems(it)
                        }
                    },
                    onDecrease = {
                        userId?.let {
                            cartRepository.removeFromCart(it, producto.id)
                            refreshCartItems(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CartItem(
    producto: Producto,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2942)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val imageRes = when {
                producto.nombre.contains("PlayStation 5", ignoreCase = true) -> R.drawable.ps5
                producto.nombre.contains("Headset", ignoreCase = true) -> R.drawable.audifonosgamer // Ejemplo\
                producto.nombre.contains("Teclado Gamer", ignoreCase = true) -> R.drawable.tecladogamer
                producto.nombre.contains("Silla Gamer Secretlab Titan", ignoreCase = true) -> R.drawable.silla
                else -> R.drawable.logolevelup
            }

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$%d".format(producto.precio),
                    color = Color(0xFFFF8C00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, enabled = quantity > 0) {
                    Icon(Icons.Filled.Remove, contentDescription = "Decrease", tint = Color.White)
                }
                Text(text = quantity.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = onIncrease) {
                    Icon(Icons.Filled.Add, contentDescription = "Increase", tint = Color.White)
                }
            }
        }
    }
}
