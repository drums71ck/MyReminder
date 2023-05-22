import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log


class DataBaseConnection(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    class PostIt(val id: Long, val title: String, val content: String, val color: String)

    companion object {

        private const val DATABASE_NAME = "myReminderDB2"
        private const val DATABASE_VERSION = 1

        // Definimos el nombre de la tabla y las columnas
        private const val TABLE_NAME = "users"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_NICKNAME = "nickname"
        private const val COLUMN_PASSWORD = "psswd"

        // Tabla del PostIt
        private const val TABLE_POSTIT_NAME = "postit"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_COLOR = "color"

    }

    override fun onCreate(db: SQLiteDatabase) {
        // Creamos la tabla en la base de datos
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_EMAIL VARCHAR(255), " +
                "$COLUMN_NICKNAME VARCHAR(255), $COLUMN_PASSWORD VARCHAR(255))"
        Log.d("pepe", createTableQuery)
        db!!.execSQL(createTableQuery)

        // Creamos la tabla de post It
        val createPostItQuery = "CREATE TABLE $TABLE_POSTIT_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$COLUMN_TITLE VARCHAR(255), $COLUMN_CONTENT VARCHAR(255), $COLUMN_COLOR VARCHAR(255))"
        Log.d("pepe","1" + createPostItQuery)
        db!!.execSQL(createPostItQuery)
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
    fun checkCredentials(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(email, password))
        val matchFound = cursor?.count ?: 0 > 0
        cursor?.close()
        db.close()
        return matchFound
    }
    fun deleteAllPostIt(): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_POSTIT_NAME, null, null)
        db.close()
        return rowsDeleted
    }

    fun insertPostIt(id: Int, title: String, content: String, color: String): Long{
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_CONTENT, content)
        values.put(COLUMN_COLOR, color)

        val db = this.writableDatabase
        Log.d("pepe2", TABLE_POSTIT_NAME)
        val id = db.insert(TABLE_POSTIT_NAME, null, values)
        db.close()

        return id

    }

    fun getAllPostIts(): List<PostIt> {
        val postItList = mutableListOf<PostIt>()

        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_POSTIT_NAME"
        val cursor: Cursor? = db.rawQuery(query, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                val color = cursor.getString(cursor.getColumnIndex(COLUMN_COLOR))

                val postIt = PostIt(id, title, content, color)
                postItList.add(postIt)
            } while (cursor.moveToNext())
        }

        cursor?.close()
        db.close()

        return postItList
    }
    fun deleteNoteById(noteId: Int): Boolean {
        val db = this.writableDatabase
        val affectedRows = db.delete(TABLE_POSTIT_NAME, "$COLUMN_ID = ?", arrayOf(noteId.toString()))
        db.close()
        return affectedRows > 0
    }
    fun deletePostItByTitle(title: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_POSTIT_NAME, "$COLUMN_TITLE = ?", arrayOf(title))
        db.close()
        return result != -1
    }

    /**
     * Esta funcion trata de recuperar en nickname del usuario en base al email que
     * tiene que ser unico
     * @param pasamos por parametro el string email del usuario
     * @return retornara el nickname buscado
     */
    fun getUsernameByEmail(email: String): String?{
        val db = readableDatabase
        val query = "SELECT $COLUMN_NICKNAME FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.rawQuery(query, selectionArgs)
        var username : String? = null
        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME))
        }
        cursor.close()
        db.close()
        return username
    }

    fun getPasswordByEmail(email: String): String?{
        val db = readableDatabase
        val query = "SELECT $COLUMN_PASSWORD FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.rawQuery(query, selectionArgs)
        var password: String? = null
        if(cursor.moveToFirst()){
            password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
        }
        cursor.close()
        db.close()
        return password
    }
    fun updatePassword(email: String, newPassword: String): Int {
        val values = ContentValues()
        values.put(COLUMN_PASSWORD, newPassword)

        val db = this.writableDatabase
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_EMAIL=?", arrayOf(email))
        db.close()

        return rowsAffected
    }




}
