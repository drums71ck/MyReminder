import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseConnection(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "myReminderDB"
        private const val DATABASE_VERSION = 1

        // Definimos el nombre de la tabla y las columnas
        private const val TABLE_NAME = "users"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_NICKNAME = "nickname"
        private const val COLUMN_PASSWORD = "psswd"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Creamos la tabla en la base de datos
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_EMAIL VARCHAR(255), " +
                "$COLUMN_NICKNAME VARCHAR(255), $COLUMN_PASSWORD VARCHAR(255))"
        db!!.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si hay una actualización de la base de datos, puedes manejarla aquí
    }

    fun insertUser(email: String, nickname: String, password: String): Long {
        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NICKNAME, nickname)
        values.put(COLUMN_PASSWORD, password)

        val db = this.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        db.close()

        return id
    }
    fun deleteAllUsers(): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_NAME, null, null)
        db.close()
        return rowsDeleted
    }
}
