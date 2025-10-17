package G1.level_up.repository

import android.content.ContentValues
import android.content.Context
import G1.level_up.LevelUpApplication
import G1.level_up.model.Producto

/**
 * `CartRepository` gestiona todas las operaciones de la base de datos relacionadas con el carrito de compras.
 * Se encarga de añadir, obtener, eliminar y limpiar productos del carrito de un usuario específico.
 *
 * @param context El contexto de la aplicación, necesario para inicializar `DatabaseHelper` y acceder
 *                a otros repositorios a través de la clase `Application`.
 */
class CartRepository(context: Context) {

    // Instancia del helper de la base de datos para interactuar con la base de datos SQLite.
    private val dbHelper = DatabaseHelper(context)
    // Instancia del repositorio de productos para obtener detalles de los productos (como el stock).
    private val productoRepository = (context.applicationContext as LevelUpApplication).productoRepository

    /**
     * Añade una unidad de un producto al carrito de un usuario.
     * Antes de añadir, comprueba si la cantidad actual en el carrito más uno no supera el stock disponible.
     *
     * @param userId El ID del usuario.
     * @param productId El ID del producto a añadir.
     * @return El ID de la fila insertada, o -1 si no se pudo añadir (por falta de stock).
     */
    fun addToCart(userId: Long, productId: Int): Long {
        val product = productoRepository.getProductById(productId)
        product?.let {
            val currentCount = getProductCountInCart(userId, productId)
            if (currentCount < it.stock) {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_CART_USER_ID, userId)
                    put(DatabaseHelper.COLUMN_CART_PRODUCT_ID, productId)
                }
                return db.insert(DatabaseHelper.TABLE_CART, null, values)
            }
        }
        return -1L // Indica que la operación falló.
    }

    /**
     * Devuelve la cantidad de un producto específico en el carrito de un usuario.
     */
    private fun getProductCountInCart(userId: Long, productId: Int): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_CART,
            null,
            "${DatabaseHelper.COLUMN_CART_USER_ID} = ? AND ${DatabaseHelper.COLUMN_CART_PRODUCT_ID} = ?",
            arrayOf(userId.toString(), productId.toString()),
            null,
            null,
            null
        )
        val count = cursor.count
        cursor.close()
        return count
    }

    /**
     * Obtiene todos los productos y sus cantidades del carrito de un usuario.
     *
     * @param userId El ID del usuario.
     * @return Un mapa donde la clave es el objeto `Producto` y el valor es la cantidad.
     */
    fun getCartItems(userId: Long): Map<Producto, Int> {
        val db = dbHelper.readableDatabase

        // Primero, obtiene todos los IDs de producto para el usuario.
        val cursor = db.query(
            DatabaseHelper.TABLE_CART,
            arrayOf(DatabaseHelper.COLUMN_CART_PRODUCT_ID),
            "${DatabaseHelper.COLUMN_CART_USER_ID} = ?",
            arrayOf(userId.toString()),
            null, null, null
        )

        // Cuenta las ocurrencias de cada ID de producto.
        val productCounts = mutableMapOf<Int, Int>()
        with(cursor) {
            while (moveToNext()) {
                val productId = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PRODUCT_ID))
                productCounts[productId] = productCounts.getOrDefault(productId, 0) + 1
            }
        }
        cursor.close()

        // Convierte los IDs de producto y sus conteos en un mapa de `Producto` y cantidad.
        val cartItems = mutableMapOf<Producto, Int>()
        for ((productId, count) in productCounts) {
            val product = productoRepository.getProductById(productId)
            product?.let { cartItems[it] = count }
        }
        return cartItems
    }

    /**
     * Elimina una sola unidad de un producto del carrito de un usuario.
     *
     * @param userId El ID del usuario.
     * @param productId El ID del producto a eliminar.
     */
    fun removeFromCart(userId: Long, productId: Int) {
        val db = dbHelper.writableDatabase
        // Elimina solo una fila que coincida con el usuario y el producto, limitando a 1.
        db.execSQL("DELETE FROM ${DatabaseHelper.TABLE_CART} WHERE ROWID IN (SELECT ROWID FROM ${DatabaseHelper.TABLE_CART} WHERE ${DatabaseHelper.COLUMN_CART_USER_ID} = ? AND ${DatabaseHelper.COLUMN_CART_PRODUCT_ID} = ? LIMIT 1)",
            arrayOf(userId.toString(), productId.toString()))
    }

    /**
     * Elimina todos los productos del carrito de un usuario.
     * Utilizado al finalizar una compra.
     *
     * @param userId El ID del usuario cuyo carrito se va a vaciar.
     */
    fun clearCart(userId: Long) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_CART, "${DatabaseHelper.COLUMN_CART_USER_ID} = ?", arrayOf(userId.toString()))
    }
}
