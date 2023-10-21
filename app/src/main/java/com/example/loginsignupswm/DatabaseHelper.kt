package com.example.loginsignupswm

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

open class DatabaseHelper(private val context: Context):
             SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){


    companion object{
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        const val COLUMN_FULLNAME = "fullname"
        const val COLUMN_EMAIL = "email"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " + // Removed PRIMARY KEY constraint
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_FULLNAME TEXT, " + // Added fullname column
                "$COLUMN_EMAIL TEXT)") // Added email column
        db?.execSQL(createTableQuery)
        Log.i("Test", "Database")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertUser(username: String, password: String, fullname: String, email: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_FULLNAME, fullname)
            put(COLUMN_EMAIL, email)
        }
        val db = writableDatabase
        return try {
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error inserting user: ${e.message}")
            -1
        }
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }
    fun isUsernameTaken(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }
    @SuppressLint("Range")
    fun updateUsername(oldUsername: String, newUsername: String): Boolean {
        val db = writableDatabase

        // Check if the new username is already taken
        if (isUsernameTaken(newUsername)) {
            return false // Username is already taken
        }

        // Get the user's existing data
        val selectQuery = "SELECT $COLUMN_FULLNAME, $COLUMN_EMAIL FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(oldUsername))

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))

            // Create a new record with the updated username
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, newUsername)
                put(COLUMN_FULLNAME, name)
                put(COLUMN_EMAIL, email)
            }

            val newRowId = db.insert(TABLE_NAME, null, values)

            // Delete the old record
            db.delete(TABLE_NAME, "$COLUMN_USERNAME = ?", arrayOf(oldUsername))

            return newRowId != -1L
        }

        cursor.close()
        return false
    }



}