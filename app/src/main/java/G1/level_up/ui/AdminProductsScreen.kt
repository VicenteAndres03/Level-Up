package G1.level_up.ui

import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.viewmodel.AdminProductsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsScreen(vm: AdminProductsViewModel = viewModel()) {
    val products by vm.products.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Producto?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark)
            .padding(16.dp)
    ) {
        Text(
            text = "Manage Products",
            color = TextColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { selectedProduct = null; showDialog = true },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Add New Product", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryAccent)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    ProductItemAdmin(
                        product = product,
                        onEdit = { selectedProduct = it; showDialog = true },
                        onDelete = { selectedProduct = it; showDeleteConfirmation = true }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ProductEditDialog(
            product = selectedProduct,
            onDismiss = { showDialog = false },
            onSave = {
                if (it.id == 0) vm.addProduct(it) else vm.updateProduct(it)
                showDialog = false
            }
        )
    }

    if (showDeleteConfirmation) {
        DeleteConfirmationDialog(
            product = selectedProduct!!,
            onDismiss = { showDeleteConfirmation = false },
            onConfirm = {
                vm.deleteProduct(it)
                showDeleteConfirmation = false
            }
        )
    }
}

@Composable
fun ProductItemAdmin(product: Producto, onEdit: (Producto) -> Unit, onDelete: (Producto) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2B47))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.imagen,
                contentDescription = product.nombre,
                placeholder = painterResource(id = R.drawable.logolevelup),
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.nombre, color = TextColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Price: ${product.precio}", color = TextColor.copy(alpha = 0.8f))
                Text(text = "Stock: ${product.stock}", color = TextColor.copy(alpha = 0.8f))
            }
            Column {
                Button(onClick = { onEdit(product) }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent)) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = { onDelete(product) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))) {
                    Text("Delete")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditDialog(product: Producto?, onDismiss: () -> Unit, onSave: (Producto) -> Unit) {
    var nombre by remember { mutableStateOf(product?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(product?.descripcion ?: "") }
    var precio by remember { mutableStateOf(product?.precio?.toString() ?: "") }
    var categoria by remember { mutableStateOf(product?.categoria ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var imagen by remember { mutableStateOf(product?.imagen ?: "") }
    var caracteristicas by remember { mutableStateOf(product?.caracteristicas ?: "") }
    var proveedor by remember { mutableStateOf(product?.proveedor ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PrimaryDark,
        titleContentColor = TextColor,
        textContentColor = TextColor,
        title = { Text(if (product == null) "Add Product" else "Edit Product") },
        text = {
            LazyColumn {
                item {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = nombre, onValueChange = { nombre = it }, label = "Name")
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = descripcion, onValueChange = { descripcion = it }, label = "Description")
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = precio, onValueChange = { precio = it }, label = "Price")
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = categoria, onValueChange = { categoria = it }, label = "Category")
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = stock, onValueChange = { stock = it }, label = "Stock")
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = imagen, onValueChange = { imagen = it }, label = "Image URL")
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = caracteristicas, onValueChange = { caracteristicas = it }, label = "Features")
                        Spacer(modifier = Modifier.height(8.dp))
                        LevelUpTextFieldDialog(value = proveedor, onValueChange = { proveedor = it }, label = "Provider")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedProduct = product?.copy(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toIntOrNull() ?: 0,
                        categoria = categoria,
                        stock = stock.toIntOrNull() ?: 0,
                        imagen = imagen,
                        caracteristicas = caracteristicas,
                        proveedor = proveedor
                    ) ?: Producto(0, nombre, descripcion, precio.toIntOrNull() ?: 0, categoria, stock.toIntOrNull() ?: 0, imagen, caracteristicas, proveedor)
                    onSave(updatedProduct)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpTextFieldDialog(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = PrimaryAccent) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryAccent,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            cursorColor = PrimaryAccent,
            focusedTextColor = TextColor,
            unfocusedTextColor = TextColor,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}


@Composable
fun DeleteConfirmationDialog(product: Producto, onDismiss: () -> Unit, onConfirm: (Producto) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PrimaryDark,
        titleContentColor = TextColor,
        textContentColor = TextColor,
        title = { Text("Delete Product") },
        text = { Text("Are you sure you want to delete ${product.nombre}?") },
        confirmButton = {
            Button(
                onClick = { onConfirm(product) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancel")
            }
        }
    )
}
