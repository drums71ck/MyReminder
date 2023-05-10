package com.example.myreminder

import DataBaseConnection
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        btnRegister.setOnClickListener(){
            cargarDatos()
        }
    }
    fun cargarDatos(){
        etEmail = findViewById(R.id.txtRegisterEmail)
        etNickname = findViewById(R.id.txtRegisterNickname)
        etPassword = findViewById(R.id.txtRegisterPassword)
        val email = etEmail.text.toString()
        val nickname = etNickname.text.toString()
        val password = etPassword.text.toString()
        print("$email $nickname $password")





        val id = dbHelper.insertUser(email, nickname, password)
        Log.d(TAG, "se a creado el usuario")

    }


    /**
     * @param
     */
    fun obtainImageByteArray(context: Context, imageResId: Int): ByteArray {
        val inputStream = context.resources.openRawResource(imageResId)
        val byteArrayOutputStream = ByteArrayOutputStream()

        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }

        return byteArrayOutputStream.toByteArray()
    }

}