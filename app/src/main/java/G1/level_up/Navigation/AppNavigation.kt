package G1.level_up.Navigation

import G1.level_up.ui.AccountScreen
import G1.level_up.ui.AdminLoginScreen
import G1.level_up.ui.AdminProductsScreen
import G1.level_up.ui.CartScreen
import G1.level_up.ui.Cuerpo
import G1.level_up.ui.Footer
import G1.level_up.ui.Header
import G1.level_up.ui.LoginScreen
import G1.level_up.ui.ProductsScreen
import G1.level_up.ui.RegisterScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * `AppNavigation` es el Composable central que gestiona la navegación de toda la aplicación.
 * Utiliza un `NavHost` para definir el grafo de navegación y un `Scaffold` para proporcionar una
 * estructura común con una barra superior (`Header`) y una barra inferior (`Footer`).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(){
    // Crea y recuerda un controlador de navegación.
    val navController = rememberNavController()
    // Observa el estado de la pila de navegación para saber cuál es la ruta actual.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    // Estado para mantener el nombre de usuario del usuario que ha iniciado sesión.
    var currentUsername by remember { mutableStateOf<String?>(null) }

    // Lista de rutas que no deben mostrar las barras superior e inferior (Header y Footer).
    val screensWithoutBars = listOf(Screen.Login.route, Screen.Register.route, Screen.AdminLogin.route, Screen.AdminProducts.route)

    // Determina el ítem seleccionado en el Footer basado en la ruta actual.
    val selectedItem = when {
        currentRoute == Screen.Home.route -> 0
        currentRoute == Screen.Productos.route -> 1
        currentRoute?.startsWith("account/") == true -> 2
        else -> 0
    }

    // Lambda para manejar el cierre de sesión.
    val onLogout = {
        currentUsername = null // Limpia el nombre de usuario.
        // Navega a la pantalla de login y limpia todo el historial de navegación anterior.
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            // Muestra el Header solo si la ruta actual no está en la lista de exclusión.
            if (currentRoute !in screensWithoutBars) {
                Header(
                    navController = navController, 
                    currentUsername = currentUsername,
                    onLogout = onLogout
                )
            }
         },
        bottomBar = {
            // Muestra el Footer solo si la ruta actual no está en la lista de exclusión.
            if (currentRoute !in screensWithoutBars) {
                Footer(
                    selectedItem = selectedItem,
                    onItemSelected = { index ->
                        when(index) {
                            0 -> navController.navigate(Screen.Home.route)
                            1 -> navController.navigate(Screen.Productos.route)
                            2 -> if (currentUsername != null) {
                                navController.navigate(Screen.Account.createRoute(currentUsername!!))
                            } else {
                                navController.navigate(Screen.Login.route)
                            }
                        }
                    },
                    loggedIn = currentUsername != null
                )
            }
        }
    ) { innerPadding ->
        // `NavHost` es el contenedor que aloja todas las pantallas (Composables) del grafo de navegación.
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route, // La pantalla inicial es el Login.
            modifier = Modifier.padding(innerPadding) // Aplica el padding del Scaffold.
        ) {
            // Define cada ruta y el Composable que se mostrará.
            composable(Screen.Home.route) {
                Cuerpo(navController = navController, currentUsername = currentUsername)
            }
            composable(Screen.Productos.route) {
                ProductsScreen(username = currentUsername)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController) { username ->
                    currentUsername = username // Actualiza el usuario logueado.
                    // Navega a Home y elimina Login de la pila para no poder volver atrás.
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController)
            }
            composable(Screen.AdminLogin.route) {
                AdminLoginScreen(navController)
            }
            composable(Screen.AdminProducts.route) {
                AdminProductsScreen(navController)
            }
            // Ruta con argumento para la pantalla de cuenta.
            composable(
                route = Screen.Account.route,
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                if (username != null) {
                    AccountScreen(username = username)
                }
            }
            // Ruta con argumento para la pantalla del carrito.
            composable(
                route = Screen.Cart.route,
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                if (username != null) {
                    CartScreen(username = username)
                }
            }
        }
    }
}