package G1.level_up.repository

import G1.level_up.model.Producto
import android.content.ContentValues
import android.content.Context
import android.database.Cursor

/**
 * `ProductoRepository` es el repositorio encargado de gestionar todas las operaciones de datos
 * para los productos. Actúa como intermediario entre los ViewModels y la fuente de datos (la base de datos SQLite).
 * Proporciona métodos CRUD (Crear, Leer, Actualizar, Eliminar) para los productos.
 *
 * @param context El contexto de la aplicación, necesario para inicializar el `DatabaseHelper`.
 */
class ProductoRepository(context: Context) {

    // Instancia del helper de la base de datos para interactuar con la base de datos SQLite.
    private val dbHelper = DatabaseHelper(context)

    /**
     * Convierte un objeto `Cursor` de la base de datos a un objeto `Producto`.
     *
     * @param cursor El cursor que apunta a una fila de la tabla de productos.
     * @return Un objeto `Producto` con los datos de la fila actual del cursor.
     */
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

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * @return Una lista de objetos `Producto`.
     */
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

    /**
     * Añade un nuevo producto a la base de datos.
     *
     * @param product El objeto `Producto` a añadir (sin ID, ya que es autoincremental).
     */
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

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param product El objeto `Producto` con los datos actualizados.
     */
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

    /**
     * Elimina un producto de la base de datos usando su ID.
     *
     * @param productId El ID del producto a eliminar.
     */
    fun deleteProduct(productId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_PRODUCTS, "${DatabaseHelper.COLUMN_PRODUCT_ID} = ?", arrayOf(productId.toString()))
    }

    /**
     * Obtiene un solo producto de la base de datos por su ID.
     *
     * @param productId El ID del producto a buscar.
     * @return El objeto `Producto` si se encuentra, o `null` si no existe.
     */
    fun getProductById(productId: Int): Producto? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_PRODUCTS,
            null, // todas las columnas
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
