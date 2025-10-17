package G1.level_up.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "level_up.db"
        private const val DATABASE_VERSION = 7 // Incremented version

        // Users table
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_USERNAME = "username"
        const val COLUMN_USER_PASSWORD = "password"

        // Cart table
        const val TABLE_CART = "cart"
        const val COLUMN_CART_ID = "id"
        const val COLUMN_CART_USER_ID = "user_id"
        const val COLUMN_CART_PRODUCT_ID = "product_id"

        // Products table
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

        // Add initial products
        addInitialProducts(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    private fun addInitialProducts(db: SQLiteDatabase) {
        val initialProducts = listOf(
            ContentValues().apply {
                put(COLUMN_PRODUCT_NAME, "Headset Gamer HyperX Cloud Alpha")
                put(COLUMN_PRODUCT_DESCRIPTION, "Audífonos con micrófono y almohadillas de espuma viscoelástica...")
                put(COLUMN_PRODUCT_PRICE, 99990)
                put(COLUMN_PRODUCT_CATEGORY, "Accesorios")
                put(COLUMN_PRODUCT_STOCK, 25)
                put(COLUMN_PRODUCT_IMAGE, "https://i.imgur.com/8o3oFm8.png")
                put(COLUMN_PRODUCT_FEATURES, "Accesorio innalambrico auditivo con micrófono y almohadillas de espuma viscoelástica...")
                put(COLUMN_PRODUCT_PROVIDER, "Hyperx")
            },
            ContentValues().apply {
                put(COLUMN_PRODUCT_NAME, "PlayStation 5")
                put(COLUMN_PRODUCT_DESCRIPTION, "La consola de última generación de Sony, que ofrece gráficos impresionantes...")
                put(COLUMN_PRODUCT_PRICE, 590000)
                put(COLUMN_PRODUCT_CATEGORY, "Consolas")
                put(COLUMN_PRODUCT_STOCK, 10)
                put(COLUMN_PRODUCT_IMAGE, "https://i.imgur.com/8o3oFm8.png")
                put(COLUMN_PRODUCT_FEATURES, "Consola de videojuegos ps5 con memoria de 1TB + FC26")
                put(COLUMN_PRODUCT_PROVIDER, "Sony")
            },
            ContentValues().apply {
                put(COLUMN_PRODUCT_NAME, "Silla Gamer Secretlab Titan")
                put(COLUMN_PRODUCT_DESCRIPTION, "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico...")
                put(COLUMN_PRODUCT_PRICE, 350990)
                put(COLUMN_PRODUCT_CATEGORY, "Sillas")
                put(COLUMN_PRODUCT_STOCK, 5)
                put(COLUMN_PRODUCT_IMAGE, "https://i.imgur.com/8o3oFm8.png")
                put(COLUMN_PRODUCT_FEATURES, "Espuma de curado en frío pendiente de patente, Apoyo óptimo independientemente de cómo te sientes")
                put(COLUMN_PRODUCT_PROVIDER, "Secret lab")
            },
            ContentValues().apply {
                put(COLUMN_PRODUCT_NAME, "Teclado Gamer")
                put(COLUMN_PRODUCT_DESCRIPTION, "Teclado rgb con teclas mecanicas...")
                put(COLUMN_PRODUCT_PRICE, 35000)
                put(COLUMN_PRODUCT_CATEGORY, "Teclado")
                put(COLUMN_PRODUCT_STOCK, 5)
                put(COLUMN_PRODUCT_IMAGE, "https://i.imgur.com/8o3oFm8.png")
                put(COLUMN_PRODUCT_FEATURES, "Espuma de curado en frío pendiente de patente, Apoyo óptimo independientemente de cómo te sientes")
                put(COLUMN_PRODUCT_PROVIDER, "LogiTech")
            }
        )

        initialProducts.forEach { values ->
            db.insert(TABLE_PRODUCTS, null, values)
        }
    }
}