package G1.level_up.Navigation

import G1.level_up.model.User
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado para mantener al usuario logueado en la memoria de la App
    var loggedInUser by remember { mutableStateOf<User?>(null) }

    Scaffold(
        topBar = {
            // No mostramos barras en Login, Registro o AdminLogin
            if (currentRoute != Screen.Login.route &&
                currentRoute != Screen.Register.route &&
                currentRoute != Screen.AdminLogin.route
            ) {
                Header(
                    navController = navController,
                    currentUsername = loggedInUser?.username,
                    onLogout = {
                        loggedInUser = null // Limpiamos la sesión
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true } // Borramos historial
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentRoute != Screen.Login.route &&
                currentRoute != Screen.Register.route &&
                currentRoute != Screen.AdminLogin.route
            ) {
                // Determinamos qué índice resaltar en el Footer basado en la ruta actual
                val selectedIndex = when {
                    currentRoute == Screen.Home.route -> 0
                    currentRoute == Screen.Productos.route -> 1
                    currentRoute?.startsWith("account") == true -> 2
                    else -> 0
                }

                Footer(
                    selectedItem = selectedIndex,
                    onItemSelected = { index ->
                        when (index) {
                            0 -> navController.navigate(Screen.Home.route)
                            1 -> navController.navigate(Screen.Productos.route)
                            2 -> {
                                loggedInUser?.let {
                                    navController.navigate(Screen.Account.createRoute(it.username))
                                } ?: navController.navigate(Screen.Login.route)
                            }
                        }
                    },
                    loggedIn = loggedInUser != null
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    navController = navController,
                    onLoginSuccess = { user ->
                        loggedInUser = user
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Home.route) {
                Cuerpo(navController, loggedInUser?.username)
            }

            composable(Screen.Productos.route) {
                loggedInUser?.let { user ->
                    ProductsScreen(user = user)
                } ?: LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route)
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
                @OptIn(ExperimentalMaterial3Api::class)
                @Composable
                fun AppNavigation() {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    // Estado único para el usuario logueado
                    var loggedInUser by remember { mutableStateOf<User?>(null) }

                    Scaffold(
                        topBar = {
                            if (currentRoute != Screen.Login.route &&
                                currentRoute != Screen.Register.route &&
                                currentRoute != Screen.AdminLogin.route
                            ) {
                                Header(
                                    navController = navController,
                                    currentUsername = loggedInUser?.username, // Parámetro correcto
                                    onLogout = {
                                        loggedInUser = null
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        },
                        bottomBar = {
                            if (currentRoute != Screen.Login.route &&
                                currentRoute != Screen.Register.route &&
                                currentRoute != Screen.AdminLogin.route
                            ) {
                                val selectedIndex = when {
                                    currentRoute == Screen.Home.route -> 0
                                    currentRoute == Screen.Productos.route -> 1
                                    currentRoute?.startsWith("account") == true -> 2
                                    else -> 0
                                }

                                Footer(
                                    selectedItem = selectedIndex,
                                    onItemSelected = { index ->
                                        when (index) {
                                            0 -> navController.navigate(Screen.Home.route)
                                            1 -> navController.navigate(Screen.Productos.route)
                                            2 -> {
                                                loggedInUser?.let {
                                                    navController.navigate(
                                                        Screen.Account.createRoute(
                                                            it.username
                                                        )
                                                    )
                                                } ?: navController.navigate(Screen.Login.route)
                                            }
                                        }
                                    },
                                    loggedIn = loggedInUser != null
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Login.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Login.route) {
                                LoginScreen(
                                    navController = navController,
                                    onLoginSuccess = { user ->
                                        loggedInUser = user
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable(Screen.Home.route) {
                                Cuerpo(navController, loggedInUser?.username)
                            }

                            composable(Screen.Productos.route) {
                                loggedInUser?.let { user ->
                                    ProductsScreen(user = user)
                                } ?: LaunchedEffect(Unit) {
                                    navController.navigate(Screen.Login.route)
                                }
                            }

                            composable(Screen.Register.route) { RegisterScreen(navController) }
                            composable(Screen.AdminLogin.route) { AdminLoginScreen(navController) }
                            composable(Screen.AdminProducts.route) {
                                AdminProductsScreen(
                                    navController
                                )
                            }

                            composable(
                                route = Screen.Account.route,
                                arguments = listOf(navArgument("username") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val username = backStackEntry.arguments?.getString("username") ?: ""
                                AccountScreen(username)
                            }

                            composable(
                                route = Screen.Cart.route,
                                arguments = listOf(navArgument("username") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val username = backStackEntry.arguments?.getString("username") ?: ""
                                CartScreen(username)
                            }
                        }
                    }
                }
            }

            composable(
                route = Screen.Account.route,
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: ""
                AccountScreen(username)
            }

            composable(
                route = Screen.Cart.route,
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: ""
                CartScreen(username)
            }
        }
    }
}
