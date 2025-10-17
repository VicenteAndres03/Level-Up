package G1.level_up.ui


import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.model.User
import G1.level_up.repository.CartRepository
import G1.level_up.repository.UserRepository
import G1.level_up.viewmodel.HomeViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(), // Obtiene la instancia de tu ViewModel
    username: String?
) {
    val productos by viewModel.productos.collectAsState()
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val cartRepository = remember { CartRepository(context) }
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(username) {
        user = if (username != null) {
            userRepository.getUserByUsername(username)
        } else {
            null
        }
    }

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
                    user?.id?.let { userId ->
                        cartRepository.addToCart(userId, producto.id)
                        Toast.makeText(context, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
                    }
                },
                isLoggedIn = user != null
            )
        }
    }
}

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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2942) // Azul un poco más claro que el fondo
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageRes = when {
                producto.nombre.contains("PlayStation 5", ignoreCase = true) -> R.drawable.ps5
                producto.nombre.contains("Headset", ignoreCase = true) -> R.drawable.audifonosgamer // Ejemplo\
                producto.nombre.contains("Teclado Gamer", ignoreCase = true) -> R.drawable.tecladogamer
                producto.nombre.contains("Silla Gamer Secretlab Titan", ignoreCase = true) -> R.drawable.silla
                else -> R.drawable.logolevelup // Imagen por defecto
            }

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.categoria,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${"%d".format(producto.precio)}", // Cambio de Double a INT
                    color = Color(0xFFFF8C00), // Naranja energético
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

                if (isLoggedIn) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onAddToCartClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Añadir", color = Color(0xFF0A1931), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

