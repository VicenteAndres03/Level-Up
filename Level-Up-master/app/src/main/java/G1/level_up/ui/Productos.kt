package G1.level_up.ui

import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.model.User
import G1.level_up.repository.CartRepository
import G1.level_up.repository.ProductoRepository // Importación esencial para el backend
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
 * ProductsScreen muestra la lista de productos obtenida desde el backend de Spring Boot.
 */
@Composable
fun ProductsScreen(
    user: User,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val productos by viewModel.productos.collectAsState() // Observa los productos del StateFlow
    val context = LocalContext.current
    val cartRepository = remember { CartRepository(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1931))
            .padding(16.dp)
    ) {
        Text(
            text = "Catálogo de Productos",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(productos) { producto ->
                ProductItem(producto = producto) {
                    // CORRECCIÓN: Se debe pasar el ID del usuario y el ID del producto.
                    // El método devuelve un Long (ID de fila) o -1L si falla.
                    val cartId = cartRepository.addToCart(user.id, producto.id)
                    if (cartId != -1L) {
                        Toast.makeText(context, "${producto.nombre} añadido", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Sin stock disponible", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(producto: Producto, onAddToCart: () -> Unit) {
    val isOutOfStock = producto.stock <= 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF152D50))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.nombre,
                placeholder = painterResource(R.drawable.logolevelup),
                error = painterResource(R.drawable.logolevelup),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = producto.nombre, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = producto.categoria, color = Color.Gray, fontSize = 14.sp)
                Text(text = "$${producto.precio}", color = Color(0xFFFF8C00), fontSize = 16.sp, fontWeight = FontWeight.Bold)

                if (isOutOfStock) {
                    Text(text = "Agotado", color = Color.Red, fontSize = 12.sp)
                } else {
                    Text(text = "Stock: ${producto.stock}", color = Color.LightGray, fontSize = 12.sp)
                }

                Button(
                    onClick = onAddToCart,
                    enabled = !isOutOfStock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8C00),
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = if (isOutOfStock) "Agotado" else "Añadir", color = Color.White)
                }
            }
        }
    }
}

/**
 * HomeViewModelFactory corregida para instanciar el Repositorio antes de pasarlo al ViewModel.
 */
class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            // Se crea el repositorio (necesario para el constructor de HomeViewModel)
            val repository = ProductoRepository(application)
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}
