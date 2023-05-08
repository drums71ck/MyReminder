package com.example.myreminder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
class DataBaseConnection (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Creamos las variables añadiendo companion para utilizarlo en todo el proyecto

    companion object{
        private const val DATABASE_NAME = "myReminderDB.db"
        private const val DATABASE_VERSION = 1
    }
    override fun onCreate(db: SQLiteDatabase) {
        // Define la estructura de la base de datos y crea las tablas necesarias
        val createTableQuery = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY,
                email TEXT,
                nickname TEXT,
                password TEXT,
                foto_perfil TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Realiza acciones adicionales cuando la estructura de la base de datos se actualiza
        // Aquí puedes añadir la lógica para migrar datos si es necesario
    }
}