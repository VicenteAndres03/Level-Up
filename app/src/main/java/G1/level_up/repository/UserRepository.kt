package G1.level_up.repository

import android.content.ContentValues
import android.content.Context
import G1.level_up.model.User

class UserRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun addUser(user: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USER_USERNAME, user.username)
            put(DatabaseHelper.COLUMN_USER_PASSWORD, user.pass)
        }
        return db.insert(DatabaseHelper.TABLE_USERS, null, values)
    }

    fun getUserByUsername(username: String): User? {
        val db = dbHelper.readableDatabase
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
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD))
            user = User(id, username, password)
        }
        cursor.close()
        return user
    }
}