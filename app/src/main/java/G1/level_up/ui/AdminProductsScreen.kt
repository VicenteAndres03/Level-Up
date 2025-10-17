package G1.level_up.ui

import G1.level_up.R
import G1.level_up.model.Producto
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminProductsScreen() {
    // Dummy list of products for demonstration
    val products = remember {
        listOf(
            Producto(1, "PlayStation 5", "La última consola de Sony", 499990, "Consolas", 10, "", "", ""),
            Producto(2, "Xbox Series X", "La consola más potente de Microsoft", 499990, "Consolas", 8, "", "", ""),
            Producto(3, "Nintendo Switch", "La consola híbrida de Nintendo", 299990, "Consolas", 15, "", "", "")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark)
            .padding(16.dp)
    ) {
        Text(
            text = "Gestionar Productos",
            color = TextColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(products) { product ->
                AdminProductItem(product = product)
            }
        }
    }
}

@Composable
fun AdminProductItem(product: Producto) {
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
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val imageRes = when {
                product.nombre.contains("PlayStation 5", ignoreCase = true) -> R.drawable.ps5
                else -> R.drawable.logolevelup
            }

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = product.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$%.2f".format(product.precio),
                    color = Color(0xFFFF8C00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Row {
                Button(onClick = { /* TODO: Handle Edit */ }) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* TODO: Handle Delete */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Eliminar")
                }
            }
        }
    }
}