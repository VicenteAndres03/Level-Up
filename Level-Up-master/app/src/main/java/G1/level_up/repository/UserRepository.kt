package G1.level_up.repository

import android.content.ContentValues
import android.content.Context
import G1.level_up.model.User

/**
 * `UserRepository` gestiona todas las operaciones de la base de datos relacionadas con los usuarios.
 * Se encarga de añadir nuevos usuarios y de obtener la información de usuarios existentes.
 *
 * @param context El contexto de la aplicación, necesario para inicializar el `DatabaseHelper`.
 */
class UserRepository(context: Context) {

    // Instancia del helper de la base de datos para interactuar con la base de datos SQLite.
    private val dbHelper = DatabaseHelper(context)

    /**
     * Añade un nuevo usuario a la base de datos.
     *
     * @param user El objeto `User` a registrar (sin ID, ya que es autoincremental).
     * @return El ID de la fila recién insertada, o -1 si ocurrió un error.
     */
    fun addUser(user: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USER_USERNAME, user.username)
            put(DatabaseHelper.COLUMN_USER_PASSWORD, user.pass)
        }
        return db.insert(DatabaseHelper.TABLE_USERS, null, values)
    }

    /**
     * Busca y devuelve un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return Un objeto `User` si se encuentra, o `null` si no existe un usuario con ese nombre.
     */
    fun getUserByUsername(username: String): User? {
        val db = dbHelper.readableDatabase
        // Realiza una consulta a la tabla de usuarios buscando una coincidencia en el nombre de usuario.
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_USERNAME, DatabaseHelper.COLUMN_USER_PASSWORD),
            "${DatabaseHelper.COLUMN_USER_USERNAME} = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        var user: User? = null
        // Si el cursor encuentra una fila, se extraen los datos.
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD))
            user = User(id, username, password) // Se crea el objeto User.
        }
        cursor.close() // Se cierra el cursor para liberar recursos.
        return user
    }
}
