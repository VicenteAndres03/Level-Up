package G1.level_up.Navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Productos : Screen("productos")
    object Login : Screen("login")
    object Register : Screen("register")
    object AdminLogin : Screen("admin_login")
    object AdminProducts : Screen("admin_products")
    object Account : Screen("account/{username}") {
        fun createRoute(username: String) = "account/$username"
    }
    object Cart : Screen("cart/{username}") {
        fun createRoute(username: String) = "cart/$username"
    }
}