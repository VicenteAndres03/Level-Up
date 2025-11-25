package G1.level_up.repository

import G1.level_up.model.Producto
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "level-up.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_USERNAME = "username"
        const val COLUMN_USER_PASSWORD = "password"

        const val TABLE_CART = "cart"
        const val COLUMN_CART_ID = "id"
        const val COLUMN_CART_USER_ID = "user_id"
        const val COLUMN_CART_PRODUCT_ID = "product_id"

        const val TABLE_PRODUCTS = "products"
        const val COLUMN_PRODUCT_ID = "id"
        const val COLUMN_PRODUCT_NAME = "name"
        const val COLUMN_PRODUCT_DESCRIPTION = "description"
        const val COLUMN_PRODUCT_PRICE = "price"
        const val COLUMN_PRODUCT_CATEGORY = "category"
        const val COLUMN_PRODUCT_STOCK = "stock"
        const val COLUMN_PRODUCT_IMAGE = "image"
        const val COLUMN_PRODUCT_FEATURES = "features"
        const val COLUMN_PRODUCT_PROVIDER = "provider"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = "CREATE TABLE $TABLE_USERS ($COLUMN_USER_ID INTEGER PRIMARY KEY, $COLUMN_USER_USERNAME TEXT, $COLUMN_USER_PASSWORD TEXT)"
        db.execSQL(createUsersTable)

        val createCartTable = "CREATE TABLE $TABLE_CART ($COLUMN_CART_ID INTEGER PRIMARY KEY, $COLUMN_CART_USER_ID INTEGER, $COLUMN_CART_PRODUCT_ID INTEGER)"
        db.execSQL(createCartTable)

        val createProductsTable = "CREATE TABLE $TABLE_PRODUCTS ($COLUMN_PRODUCT_ID INTEGER PRIMARY KEY, $COLUMN_PRODUCT_NAME TEXT, $COLUMN_PRODUCT_DESCRIPTION TEXT, $COLUMN_PRODUCT_PRICE INTEGER, $COLUMN_PRODUCT_CATEGORY TEXT, $COLUMN_PRODUCT_STOCK INTEGER, $COLUMN_PRODUCT_IMAGE TEXT, $COLUMN_PRODUCT_FEATURES TEXT, $COLUMN_PRODUCT_PROVIDER TEXT)"
        db.execSQL(createProductsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    private fun cursorToProducto(cursor: Cursor): Producto {
        return Producto(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
            descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION)),
            precio = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)),
            categoria = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY)),
            stock = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_STOCK)),
            imagen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE)),
            caracteristicas = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_FEATURES)),
            proveedor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PROVIDER))
        )
    }

    fun getProductByIdLocal(productId: Int): Producto? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS,
            null,
            "$COLUMN_PRODUCT_ID = ?",
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

    fun obtenerProductosLocal(): List<Producto> {
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null)
        val products = mutableListOf<Producto>()
        with(cursor) {
            while (moveToNext()) {
                products.add(cursorToProducto(this))
            }
        }
        cursor.close()
        return products
    }

    // [NUEVA FUNCIÓN AÑADIDA]
    fun addProductLocal(product: Producto): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            // No se pasa el ID, permitiendo que SQLite lo genere automáticamente
            put(COLUMN_PRODUCT_NAME, product.nombre)
            put(COLUMN_PRODUCT_DESCRIPTION, product.descripcion)
            put(COLUMN_PRODUCT_PRICE, product.precio)
            put(COLUMN_PRODUCT_CATEGORY, product.categoria)
            put(COLUMN_PRODUCT_STOCK, product.stock)
            put(COLUMN_PRODUCT_IMAGE, product.imagen)
            put(COLUMN_PRODUCT_FEATURES, product.caracteristicas)
            put(COLUMN_PRODUCT_PROVIDER, product.proveedor)
        }
        val newId = db.insert(TABLE_PRODUCTS, null, values)
        Log.d("DatabaseHelper", "Producto guardado localmente con ID: $newId")
        return newId
    }

    fun syncProducts(products: List<Producto>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete(TABLE_PRODUCTS, null, null)
            products.forEach { product ->
                val values = ContentValues().apply {
                    put(COLUMN_PRODUCT_ID, product.id)
                    put(COLUMN_PRODUCT_NAME, product.nombre)
                    put(COLUMN_PRODUCT_DESCRIPTION, product.descripcion)
                    put(COLUMN_PRODUCT_PRICE, product.precio)
                    put(COLUMN_PRODUCT_CATEGORY, product.categoria)
                    put(COLUMN_PRODUCT_STOCK, product.stock)
                    put(COLUMN_PRODUCT_IMAGE, product.imagen)
                    put(COLUMN_PRODUCT_FEATURES, product.caracteristicas)
                    put(COLUMN_PRODUCT_PROVIDER, product.proveedor)
                }
                db.insertWithOnConflict(TABLE_PRODUCTS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}