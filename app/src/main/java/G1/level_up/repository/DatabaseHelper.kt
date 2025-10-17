package G1.level_up.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "level_up.db"

        private const val DATABASE_VERSION = 5

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
        const val COLUMN_PRODUCT_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Creación de tabla de usuarios
        val createUsersTable = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER_USERNAME TEXT UNIQUE, " +
                "$COLUMN_USER_PASSWORD TEXT)"
        db.execSQL(createUsersTable)

        // Creación de tabla de carrito (CORREGIDA)
        val createCartTable = "CREATE TABLE $TABLE_CART (" +
                "$COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_CART_USER_ID INTEGER, " +
                "$COLUMN_CART_PRODUCT_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_CART_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID))"
        db.execSQL(createCartTable)

        // Creación de tabla de productos
        val createProductsTable = "CREATE TABLE $TABLE_PRODUCTS (" +
                "$COLUMN_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_PRODUCT_NAME TEXT, " +
                "$COLUMN_PRODUCT_PRICE INTEGER)"
        db.execSQL(createProductsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }
}