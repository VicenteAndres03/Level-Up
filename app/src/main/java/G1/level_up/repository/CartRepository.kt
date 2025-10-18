package G1.level_up.repository

import android.content.ContentValues
import android.content.Context
import G1.level_up.LevelUpApplication
import G1.level_up.model.Producto

class CartRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val productoRepository = (context.applicationContext as LevelUpApplication).productoRepository

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
        return -1L
    }

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

    fun getCartItems(userId: Long): Map<Producto, Int> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_CART,
            arrayOf(DatabaseHelper.COLUMN_CART_PRODUCT_ID),
            "${DatabaseHelper.COLUMN_CART_USER_ID} = ?",
            arrayOf(userId.toString()),
            null, null, null
        )

        val productCounts = mutableMapOf<Int, Int>()
        with(cursor) {
            while (moveToNext()) {
                val productId = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PRODUCT_ID))
                productCounts[productId] = productCounts.getOrDefault(productId, 0) + 1
            }
        }
        cursor.close()

        val cartItems = mutableMapOf<Producto, Int>()
        for ((productId, count) in productCounts) {
            val product = productoRepository.getProductById(productId)
            product?.let { cartItems[it] = count }
        }
        return cartItems
    }

    fun removeFromCart(userId: Long, productId: Int) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${DatabaseHelper.TABLE_CART} WHERE ROWID IN (SELECT ROWID FROM ${DatabaseHelper.TABLE_CART} WHERE ${DatabaseHelper.COLUMN_CART_USER_ID} = ? AND ${DatabaseHelper.COLUMN_CART_PRODUCT_ID} = ? LIMIT 1)",
            arrayOf(userId.toString(), productId.toString()))
    }

    fun removeAllUnitsOfProductFromCart(userId: Long, productId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_CART, "${DatabaseHelper.COLUMN_CART_USER_ID} = ? AND ${DatabaseHelper.COLUMN_CART_PRODUCT_ID} = ?", arrayOf(userId.toString(), productId.toString()))
    }

    fun clearCart(userId: Long) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_CART, "${DatabaseHelper.COLUMN_CART_USER_ID} = ?", arrayOf(userId.toString()))
    }
}