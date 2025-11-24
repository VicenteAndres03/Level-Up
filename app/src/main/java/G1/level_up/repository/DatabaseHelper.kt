package G1.level_up.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * `DatabaseHelper` es una clase que extiende `SQLiteOpenHelper` para gestionar la creación y actualización
 * de la base de datos SQLite de la aplicación. Centraliza la definición del esquema de la BD (tablas y columnas).
 *
 * @param context El contexto de la aplicación.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * `companion object` contiene las constantes que definen el nombre de la base de datos,
     * la versión y el esquema (nombres de tablas y columnas). Esto evita errores tipográficos
     * al usar estas cadenas en diferentes partes del código.
     */
    companion object {
        private const val DATABASE_NAME = "level_up.db"
        private const val DATABASE_VERSION = 9 // Se incrementa cada vez que hay un cambio en el esquema.

        // Tabla de Usuarios
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_USERNAME = "username"
        const val COLUMN_USER_PASSWORD = "password"

        // Tabla del Carrito
        const val TABLE_CART = "cart"
        const val COLUMN_CART_ID = "id"
        const val COLUMN_CART_USER_ID = "user_id"
        const val COLUMN_CART_PRODUCT_ID = "product_id"

        // Tabla de Productos
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

    /**
     * `onCreate` se llama la primera vez que se accede a la base de datos.
     * Aquí es donde se deben ejecutar las sentencias SQL para crear las tablas.
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = "CREATE TABLE $TABLE_USERS ($COLUMN_USER_ID INTEGER PRIMARY KEY, $COLUMN_USER_USERNAME TEXT, $COLUMN_USER_PASSWORD TEXT)"
        db.execSQL(createUsersTable)

        val createCartTable = "CREATE TABLE $TABLE_CART ($COLUMN_CART_ID INTEGER PRIMARY KEY, $COLUMN_CART_USER_ID INTEGER, $COLUMN_CART_PRODUCT_ID INTEGER)"
        db.execSQL(createCartTable)

        val createProductsTable = "CREATE TABLE $TABLE_PRODUCTS ($COLUMN_PRODUCT_ID INTEGER PRIMARY KEY, $COLUMN_PRODUCT_NAME TEXT, $COLUMN_PRODUCT_DESCRIPTION TEXT, $COLUMN_PRODUCT_PRICE INTEGER, $COLUMN_PRODUCT_CATEGORY TEXT, $COLUMN_PRODUCT_STOCK INTEGER, $COLUMN_PRODUCT_IMAGE TEXT, $COLUMN_PRODUCT_FEATURES TEXT, $COLUMN_PRODUCT_PROVIDER TEXT)"
        db.execSQL(createProductsTable)

        // Se añaden productos iniciales para que la app no empiece vacía.
        addInitialProducts(db)
    }

    /**
     * `onUpgrade` se llama cuando la `DATABASE_VERSION` es mayor que la versión de la base de datos existente.
     * La estrategia aquí es simple: eliminar las tablas antiguas y volver a crearlas.
     * En una app de producción, se implementaría una migración de datos para no perder la información del usuario.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    /**
     * `addInitialProducts` inserta un conjunto de productos predefinidos en la tabla de productos.
     * Esto es útil para tener datos de demostración al instalar la aplicación por primera vez.
     */
    private fun addInitialProducts(db: SQLiteDatabase) {
        val initialProducts = listOf(
            ContentValues().apply {
                put(COLUMN_PRODUCT_NAME, "Headset Gamer HyperX Cloud Alpha")
                put(COLUMN_PRODUCT_DESCRIPTION, "Audífonos con micrófono y almohadillas de espuma viscoelástica con tecnología HyperX Dual Chamber para un sonido más nítido y menos distorsión.")
                put(COLUMN_PRODUCT_PRICE, 99990)
                put(COLUMN_PRODUCT_CATEGORY, "Audio")
                put(COLUMN_PRODUCT_STOCK, 3)
                put(COLUMN_PRODUCT_IMAGE, "https://media.solotodo.com/media/products/133461_picture_1652988450.webp")
                put(COLUMN_PRODUCT_FEATURES, "Sonido 7.1, Inalámbrico, Cancelación de ruido")
                put(COLUMN_PRODUCT_PROVIDER, "HyperX")
            },
            ContentValues().apply {
                put(COLUMN_PRODUCT_NAME, "Mouse Gamer Logitech G502")
                put(COLUMN_PRODUCT_DESCRIPTION, "Mouse para juegos de alto rendimiento con sensor HERO 25K, el sensor para juegos más preciso de Logitech hasta la fecha. Con 11 botones programables, iluminación RGB LIGHTSYNC y pesas ajustables.")
                put(COLUMN_PRODUCT_PRICE, 69990)
                put(COLUMN_PRODUCT_CATEGORY, "Periféricos")
                put(COLUMN_PRODUCT_STOCK, 25)
                put(COLUMN_PRODUCT_IMAGE, "https://media.solotodo.com/media/products/56793_picture_1583595568.webp")
                put(COLUMN_PRODUCT_FEATURES, "11 botones programables, Pesas ajustables, RGB Lightsync")
                put(COLUMN_PRODUCT_PROVIDER, "Logitech")
            }
        )

        initialProducts.forEach { values ->
            db.insert(TABLE_PRODUCTS, null, values)
        }
    }
}
