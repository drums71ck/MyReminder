package com.example.myreminder

import DataBaseConnection
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream


class Register : AppCompatActivity() {
    // obtenemos los datos de los edits layouts
    lateinit var etEmail : TextInputEditText
    lateinit var etNickname : TextInputEditText
    lateinit var etPassword : TextInputEditText
    lateinit var btnUpRegister : Button
    lateinit var dbHelper: DataBaseConnection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btnRegister = findViewById<Button>(R.id.btnUpRegister)
        dbHelper = DataBaseConnection(this)
        //dbHelper.deleteAllUsers()
        btnRegister.setOnClickListener(){
            cargarDatos()
        }
    }
    fun cargarDatos(){

            etEmail = findViewById(R.id.txtRegisterEmail)
            etNickname = findViewById(R.id.txtRegisterNickname)
            etPassword = findViewById(R.id.txtRegisterPassword)

            val eInputEmail : TextInputLayout = etEmail.parent.parent as TextInputLayout
            val eInputPassword : TextInputLayout = etPassword.parent.parent as TextInputLayout


            val email = etEmail.text.toString()
            val nickname = etNickname.text.toString()
            val password = etPassword.text.toString()

            val isEmailIsDuplicate = checkEmailDuplicate(email)


            // chequeamos datos
        // Chequeamos los datos
        if (isEmailIsDuplicate) {
            eInputEmail.error = "the email is already used"

        } else {
            eInputEmail.isErrorEnabled = false
        }

        if (password.length < 5 || !password.contains(Regex("[a-zA-Z]"))) {
            eInputPassword.error = "the password may have many less than 5 number a any letter "

        } else {
            eInputPassword.isErrorEnabled = false
        }

        // Verificamos si ambos campos tienen errores
        val hasEmailError = eInputEmail.isErrorEnabled
        val hasPasswordError = eInputPassword.isErrorEnabled

        if (!hasEmailError && !hasPasswordError) {
            print("$email $nickname $password")
            val id = dbHelper.insertUser(email, nickname, password)
            Toast.makeText(this, "¡The user have been created!", Toast.LENGTH_SHORT).show()
            // Le diremos que vaya al Login
            goToLogin()
        }


    }
    fun checkEmailDuplicate(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM users WHERE email = ?", arrayOf(email))
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }
    fun goToLogin(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    /**
     * @param
     */


}