package G1.level_up.model

/**
 * `User` es una clase de datos (data class) que representa el modelo de un usuario en la aplicación.
 * Al ser una `data class`, Kotlin genera automáticamente funciones útiles como `equals()`, `hashCode()`, `toString()` y `copy()`.
 *
 * @property id El identificador único del usuario en la base de datos (Long).
 * @property username El nombre de usuario, que debe ser único.
 * @property pass La contraseña del usuario.
 */
data class User(val id: Long, val username: String, val pass: String)
