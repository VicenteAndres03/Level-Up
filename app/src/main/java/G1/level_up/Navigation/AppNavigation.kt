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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var currentUsername by remember { mutableStateOf<String?>(null) }

    // Screens that shouldn't show the header and footer
    val screensWithoutBars = listOf(Screen.Login.route, Screen.Register.route, Screen.AdminLogin.route, Screen.AdminProducts.route)

    val selectedItem = when {
        currentRoute == Screen.Home.route -> 0
        currentRoute == Screen.Productos.route -> 1
        currentRoute?.startsWith("account/") == true -> 2
        else -> 0
    }

    val onLogout = {
        currentUsername = null
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            if (currentRoute !in screensWithoutBars) {
                Header(
                    navController = navController, 
                    currentUsername = currentUsername,
                    onLogout = onLogout
                )
            }
         },
        bottomBar = {
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
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                Cuerpo(navController = navController, currentUsername = currentUsername)
            }
            composable(Screen.Productos.route) {
                ProductsScreen(username = currentUsername)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController) { username ->
                    currentUsername = username
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
                AdminProductsScreen()
            }
            composable(
                route = Screen.Account.route,
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                if (username != null) {
                    AccountScreen(username = username)
                }
            }
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