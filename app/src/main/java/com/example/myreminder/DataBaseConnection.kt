import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log


/**
 * Esta clase sera nuestro controlador y conexion a la base
 * de datos de las tablas y usuarios
 */
class DataBaseConnection(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    /**
     * Esta clase solo se encarga de ser el modelo de la nota
     */
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

    /**
     * En el onCreate es que cuando se ponga en marcha la app
     * se creara las tablas (si es que no hay una ya almacenada)
     */
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

    /**
     * La función insertUser lo que hara pasandole parametros añadira un nuevo usuario
     * @param email el correo del usuario
     * @param nickname el nombre del usuario
     * @param password la contraseña del usuario
     * @return retornara el id del usuario creado
     */
    fun insertUser(email: String, nickname: String, password: String): Long {
        /*
        Los parametros que se pongan lo guardara y
        los metera con respecto a su columna
         */
        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NICKNAME, nickname)
        values.put(COLUMN_PASSWORD, password)

        // Escribira la base de datos
        val db = this.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        db.close()

        return id
    }

    /**
     * Esta función solo se usa si se quiere purgar el server.
     */
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
    /**
     * Esta función solo verificara que no haya un correo
     * ya registrado
     *
     * @param email
     * @param password
     * @return
     */
    fun deleteAllPostIt(): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_POSTIT_NAME, null, null)
        db.close()
        return rowsDeleted
    }

    /**
     * Esta función como el insertUser tomara los parametros para rellenar la
     * tabla postIt con sus respectivos valores
     * @param id
     * @param title
     * @param content
     * @param color
     * @return el id del post it creado
     */
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

    /**
     * Esta función lo que hace es que obtendra cada post it creado
     * y la guardara en una list para si hay uno que ya se habia creado
     * ponerlo en el fragment home
     *
     * @return la lista de los post it
     */
    fun getAllPostIts(): List<PostIt> {
        val postItList = mutableListOf<PostIt>()

        // que selecione todo sobre la tabla post it
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_POSTIT_NAME"
        val cursor: Cursor? = db.rawQuery(query, null)

        // Obtener los datos de cada tabla
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

    /**
     * Esta función lo que hara es borrar los datos
     * del post-it
     *
     * @param title
     * @return
     */
    fun deletePostItByTitle(title: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_POSTIT_NAME, "$COLUMN_TITLE = ?", arrayOf(title))
        db.close()
        return result != -1
    }
    fun deleteUserByEmail(email: String): Boolean{
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_EMAIL = ?", arrayOf(email))
        db.close()
        return result != -1
    }

    /**
     * Esta funcion trata de recupe)rar en nickname del usuario en base al email que
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

    /**
     * Esta función es obtener el password del usuario
     * mediante el email ya que no se puede duplicar el email
     * sera mas facil encontrarlo
     *
     * @param email
     * @return
     */

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

    /**
     * Esta función esta acompañada con la clase minigame
     * lo que hace es que por los parametros que recibe
     * actualizara el password nuevo que puso el usuario
     *
     * @param email
     * @param newPassword
     * @return
     */
    fun updatePassword(email: String, newPassword: String): Int {
        val values = ContentValues()
        values.put(COLUMN_PASSWORD, newPassword)

        val db = this.writableDatabase
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_EMAIL=?", arrayOf(email))
        db.close()

        return rowsAffected
    }




}
