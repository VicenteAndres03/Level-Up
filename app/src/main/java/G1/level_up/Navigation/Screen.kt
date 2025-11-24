package G1.level_up.Navigation

/**
 * `Screen` es una clase sellada (`sealed class`) que define todas las rutas de navegación de la aplicación.
 * El uso de una clase sellada garantiza que todas las posibles pantallas estén definidas en este archivo,
 * lo que proporciona seguridad de tipos y previene errores por rutas inexistentes en tiempo de compilación.
 *
 * @property route La cadena de texto que representa la ruta única para cada pantalla.
 */
sealed class Screen(val route: String) {
    // Define la pantalla de inicio.
    object Home : Screen("home")

    // Define la pantalla de productos (actualmente podría no estar en uso directo).
    object Productos : Screen("productos")

    // Define la pantalla de inicio de sesión para usuarios.
    object Login : Screen("login")

    // Define la pantalla de registro para nuevos usuarios.
    object Register : Screen("register")

    // Define la pantalla de inicio de sesión para administradores.
    object AdminLogin : Screen("admin_login")

    // Define la pantalla de gestión de productos para administradores.
    object AdminProducts : Screen("admin_products")

    // Define la pantalla de la cuenta de usuario. Esta ruta requiere un argumento `username`.
    object Account : Screen("account/{username}") {
        /**
         * Crea la ruta completa para la pantalla de cuenta, incluyendo el nombre de usuario.
         * @param username El nombre de usuario a pasar a la ruta.
         * @return La ruta formateada (ej: "account/testuser").
         */
        fun createRoute(username: String) = "account/$username"
    }

    // Define la pantalla del carrito de compras. También requiere el `username`.
    object Cart : Screen("cart/{username}") {
        /**
         * Crea la ruta completa para la pantalla del carrito, incluyendo el nombre de usuario.
         * @param username El nombre de usuario a pasar a la ruta.
         * @return La ruta formateada (ej: "cart/testuser").
         */
        fun createRoute(username: String) = "cart/$username"
    }
}
