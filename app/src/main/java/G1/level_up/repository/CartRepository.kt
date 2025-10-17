package G1.level_up.repository

import android.content.ContentValues
import android.content.Context
import G1.level_up.model.Producto

class CartRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun addToCart(userId: Long, productId: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CART_USER_ID, userId)
            put(DatabaseHelper.COLUMN_CART_PRODUCT_ID, productId)
        }
        return db.insert(DatabaseHelper.TABLE_CART, null, values)
    }

    fun getCartItems(userId: Long): Map<Producto, Int> {
        val db = dbHelper.readableDatabase
        val productRepository = ProductoRepository()

        val cursor = db.query(
            DatabaseHelper.TABLE_CART,
            arrayOf(DatabaseHelper.COLUMN_CART_PRODUCT_ID),
            "${DatabaseHelper.COLUMN_CART_USER_ID} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        val productCounts = mutableMapOf<Int, Int>()
        with(cursor) {
            while (moveToNext()) {
                val productId = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PRODUCT_ID))
                productCounts[productId] = productCounts.getOrDefault(productId, 0) + 1
            }
        }
        cursor.close()

        val allProducts = productRepository.obtenerProductos()
        val cartItems = mutableMapOf<Producto, Int>()
        for ((productId, count) in productCounts) {
            val product = allProducts.find { it.id == productId }
            product?.let { cartItems[it] = count }
        }
        return cartItems
    }

    fun removeFromCart(userId: Long, productId: Int) {
        val db = dbHelper.writableDatabase
        // This will delete one entry for the given user and product.
        // It relies on the ROWID, which is a unique id for each row in a table.
        db.execSQL("DELETE FROM ${DatabaseHelper.TABLE_CART} WHERE ROWID IN (SELECT ROWID FROM ${DatabaseHelper.TABLE_CART} WHERE ${DatabaseHelper.COLUMN_CART_USER_ID} = ? AND ${DatabaseHelper.COLUMN_CART_PRODUCT_ID} = ? LIMIT 1)",
            arrayOf(userId.toString(), productId.toString()))
    }
}