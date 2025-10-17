package G1.level_up.repository

import G1.level_up.model.Producto
import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class ProductoRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    private fun cursorToProducto(cursor: Cursor): Producto {
        return Producto(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME)),
            descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION)),
            precio = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE)),
            categoria = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY)),
            stock = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK)),
            imagen = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE)),
            caracteristicas = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_FEATURES)),
            proveedor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PROVIDER))
        )
    }

    fun obtenerProductos(): List<Producto> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, null, null, null, null, null)
        val products = mutableListOf<Producto>()
        with(cursor) {
            while (moveToNext()) {
                products.add(cursorToProducto(this))
            }
        }
        cursor.close()
        return products
    }

    fun addProduct(product: Producto) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PRODUCT_NAME, product.nombre)
            put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, product.descripcion)
            put(DatabaseHelper.COLUMN_PRODUCT_PRICE, product.precio)
            put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY, product.categoria)
            put(DatabaseHelper.COLUMN_PRODUCT_STOCK, product.stock)
            put(DatabaseHelper.COLUMN_PRODUCT_IMAGE, product.imagen)
            put(DatabaseHelper.COLUMN_PRODUCT_FEATURES, product.caracteristicas)
            put(DatabaseHelper.COLUMN_PRODUCT_PROVIDER, product.proveedor)
        }
        db.insert(DatabaseHelper.TABLE_PRODUCTS, null, values)
    }

    fun updateProduct(product: Producto) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PRODUCT_NAME, product.nombre)
            put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, product.descripcion)
            put(DatabaseHelper.COLUMN_PRODUCT_PRICE, product.precio)
            put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY, product.categoria)
            put(DatabaseHelper.COLUMN_PRODUCT_STOCK, product.stock)
            put(DatabaseHelper.COLUMN_PRODUCT_IMAGE, product.imagen)
            put(DatabaseHelper.COLUMN_PRODUCT_FEATURES, product.caracteristicas)
            put(DatabaseHelper.COLUMN_PRODUCT_PROVIDER, product.proveedor)
        }
        db.update(DatabaseHelper.TABLE_PRODUCTS, values, "${DatabaseHelper.COLUMN_PRODUCT_ID} = ?", arrayOf(product.id.toString()))
    }

    fun deleteProduct(productId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_PRODUCTS, "${DatabaseHelper.COLUMN_PRODUCT_ID} = ?", arrayOf(productId.toString()))
    }
    fun getProductById(productId: Int): Producto? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_PRODUCTS,
            null, // all columns
            "${DatabaseHelper.COLUMN_PRODUCT_ID} = ?",
            arrayOf(productId.toString()),
            null, null, null, "1"
        )
        var product: Producto? = null
        if (cursor.moveToFirst()) {
            product = cursorToProducto(cursor)
        }
        cursor.close()
        return product
    }
}
