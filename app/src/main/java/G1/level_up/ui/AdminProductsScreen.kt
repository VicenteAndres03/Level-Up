package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.R
import G1.level_up.model.Producto
import G1.level_up.ui.theme.*
import G1.level_up.viewmodel.AdminProductsViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
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
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsScreen(navController: NavController, vm: AdminProductsViewModel = viewModel()) {
    val products by vm.products.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Producto?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = PrimaryDark,
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Productos", color = TextColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryDark),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión", tint = TextColor)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { selectedProduct = null; showDialog = true },
                containerColor = ButtonAccent,
                contentColor = PrimaryDark
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryAccent)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductItemAdmin(
                            product = product,
                            onEdit = { selectedProduct = it; showDialog = true },
                            onDelete = { selectedProduct = it; showDeleteConfirmation = true }
                        )
                    }
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imagen,
                contentDescription = product.nombre,
                placeholder = painterResource(id = R.drawable.logolevelup),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nombre,
                    color = TextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio: $${product.precio}",
                    color = TextColor.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Text(
                    text = "Stock: ${product.stock}",
                    color = when {
                        product.stock == 0 -> DestructiveColor
                        product.stock <= 5 -> Color.Yellow
                        else -> TextColor.copy(alpha = 0.8f)
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                IconButton(onClick = { onEdit(product) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = PrimaryAccent)
                }
                IconButton(onClick = { onDelete(product) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = DestructiveColor)
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
        title = { Text(if (product == null) "Añadir Producto" else "Editar Producto") },
        text = {
            LazyColumn {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        LevelUpTextFieldDialog(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
                        LevelUpTextFieldDialog(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción")
                        LevelUpTextFieldDialog(value = precio, onValueChange = { precio = it }, label = "Precio")
                        LevelUpTextFieldDialog(value = categoria, onValueChange = { categoria = it }, label = "Categoría")
                        LevelUpTextFieldDialog(value = stock, onValueChange = { stock = it }, label = "Stock")
                        LevelUpTextFieldDialog(value = imagen, onValueChange = { imagen = it }, label = "URL de la Imagen")
                        LevelUpTextFieldDialog(value = caracteristicas, onValueChange = { caracteristicas = it }, label = "Características")
                        LevelUpTextFieldDialog(value = proveedor, onValueChange = { proveedor = it }, label = "Proveedor")
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
                Text("Guardar", color = PrimaryDark)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancelar")
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
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = TextColor,
            unfocusedLabelColor = TextColor.copy(alpha = 0.7f),
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
        title = { Text("Eliminar Producto") },
        text = { Text("¿Estás seguro de que quieres eliminar ${product.nombre}?") },
        confirmButton = {
            Button(
                onClick = { onConfirm(product) },
                colors = ButtonDefaults.buttonColors(containerColor = DestructiveColor)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancelar")
            }
        }
    )
}
