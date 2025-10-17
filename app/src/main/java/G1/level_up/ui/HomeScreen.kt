package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * `Header` es el Composable para la barra de aplicación superior (TopAppBar).
 * Muestra el logo, un menú de navegación desplegable y un ícono para el carrito de compras.
 *
 * @param navController Controlador para la navegación.
 * @param currentUsername Nombre del usuario logueado, o null si no hay sesión.
 * @param onLogout Lambda que se ejecuta al cerrar sesión.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(navController: NavController, currentUsername: String?, onLogout: () -> Unit){
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(painter = painterResource(id = R.drawable.logolevelup), contentDescription = "Level-Up Gamer", modifier = Modifier.height(50.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Level-Up Gamer", color = Color.White)
            }
        },
        navigationIcon = { // Ícono a la izquierda del título, para el menú.
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                }
                // Menú desplegable que se muestra/oculta según el estado `menuExpanded`.
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Inicio")}, onClick = { navController.navigate(Screen.Home.route); menuExpanded = false })
                    DropdownMenuItem(text = { Text("Productos")}, onClick = { navController.navigate(Screen.Productos.route); menuExpanded = false })
                    // Opciones que solo aparecen si el usuario ha iniciado sesión.
                    if (currentUsername != null) {
                        DropdownMenuItem(text = { Text("Cuenta") }, onClick = { navController.navigate(Screen.Account.createRoute(currentUsername)); menuExpanded = false })
                        DropdownMenuItem(text = { Text("Cerrar Sesión") }, onClick = { onLogout(); menuExpanded = false })
                    } else {
                        // Opción para iniciar sesión si no hay usuario logueado.
                        DropdownMenuItem(text = { Text("Iniciar Sesión") }, onClick = { navController.navigate(Screen.Login.route); menuExpanded = false })
                    }
                }
            }
        },
        actions = { // Acciones a la derecha del título.
            // El ícono del carrito solo se muestra si hay un usuario logueado.
            if (currentUsername != null) {
                IconButton(onClick = { navController.navigate(Screen.Cart.createRoute(currentUsername)) }) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00A9E0))
    )
}

/**
 * `Cuerpo` es el contenido principal de la pantalla de inicio.
 * Muestra tarjetas de acción (`ActionCard`) que actúan como accesos directos a otras pantallas.
 */
@Composable
fun Cuerpo(navController: NavController, currentUsername: String?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color(0xFF0A1931), Color(0xFF061121)))).padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ActionCard(
            title = "Nuestros productos a un buen precio",
            imageRes = R.drawable.ps5,
            buttonText = "Ver Productos",
            onClick = { navController.navigate(Screen.Productos.route) }
        )
        ActionCard(
            title = "Conoce los Datos de tu cuenta",
            imageRes = R.drawable.login,
            buttonText = if (currentUsername != null) "Ver Cuenta" else "Ingresar",
            onClick = {
                if (currentUsername != null) {
                    navController.navigate(Screen.Account.createRoute(currentUsername))
                } else {
                    navController.navigate(Screen.Login.route)
                }
            }
        )
    }
}

/**
 * `ActionCard` es una tarjeta reutilizable con una imagen de fondo y un botón.
 * Sirve como un llamado a la acción visualmente atractivo.
 */
@Composable
fun ActionCard(title: String, imageRes: Int, buttonText: String, onClick: () -> Unit) {
    Text(modifier = Modifier.padding(horizontal = 32.dp), text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(200.dp), shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            // Degradado oscuro en la parte inferior para que el botón sea más legible.
            Box(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 300f)))
            Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                Button(onClick = onClick, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00))) {
                    Text(text = buttonText, color = Color(0xFF0A1931), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * `NavigationItem` es una clase de datos para contener la información de cada ítem de la barra de navegación.
 */
data class NavigationItem(val label: String, val icon: ImageVector)

/**
 * `Footer` es el Composable para la barra de navegación inferior.
 * Sus ítems cambian dependiendo de si el usuario ha iniciado sesión o no.
 *
 * @param selectedItem El índice del ítem actualmente seleccionado.
 * @param onItemSelected Lambda que se invoca cuando se selecciona un ítem.
 * @param loggedIn Booleano que indica el estado de sesión del usuario.
 */
@Composable
fun Footer(selectedItem: Int, onItemSelected: (Int) -> Unit, loggedIn: Boolean){
    // La lista de ítems cambia dinámicamente.
    val items = if (loggedIn) {
        listOf(
            NavigationItem("Inicio", Icons.Default.Home),
            NavigationItem("Productos", Icons.Default.ShoppingCart),
            NavigationItem("Cuenta", Icons.Default.Person)
        )
    } else {
        listOf(
            NavigationItem("Inicio", Icons.Default.Home),
            NavigationItem("Productos", Icons.Default.ShoppingCart),
            NavigationItem("Iniciar Sesión", Icons.Default.Person)
        )
    }

    NavigationBar(containerColor = Color(0xFF0A1931)) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = (selectedItem == index),
                onClick = { onItemSelected(index) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFF8C00),
                    selectedTextColor = Color(0xFFFF8C00),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}