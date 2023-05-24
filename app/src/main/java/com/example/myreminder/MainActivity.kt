package com.example.myreminder

import DataBaseConnection
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.provider.Settings
class MainActivity : AppCompatActivity() {

    lateinit var btnRegister: Button
    lateinit var btnLogin: Button
    lateinit var lEmail: TextInputEditText
    lateinit var lPassword: TextInputEditText
    lateinit var dbHelper: DataBaseConnection
    private val NOTIFICATION_PERMISSION_CODE = 1001

    companion object{
        lateinit var bestEmail: String
    }

    private val WRITE_PERMISSION_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DataBaseConnection(this)
       // dbHelper.deleteAllPostIt()
        setContentView(R.layout.activity_main)
        cargarBtn()
        requestNotificationPermissions()


    }

    private fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val areNotificationPermissionsGranted = notificationManager.areNotificationsEnabled()

            if (!areNotificationPermissionsGranted) {
                // Los permisos de notificación no están habilitados, se solicitan al usuario
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                startActivityForResult(intent, NOTIFICATION_PERMISSION_CODE)
            } else {
                // Los permisos de notificación están habilitados
                // Puedes continuar con la lógica de tu aplicación
            }
        } else {
            // Versiones anteriores a Android O no requieren permisos explícitos para las notificaciones
            // Puedes continuar con la lógica de tu aplicación
        }
    }


    fun cargarBtn() {
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister.setOnClickListener() {
            goToRegister()
        }
        btnLogin.setOnClickListener() {
            checkEmailAndPassword()
        }

    }


    private fun checkEmailAndPassword() {
        lEmail = findViewById(R.id.txtLoginEmail)
        lPassword = findViewById(R.id.txtLoginPassword)

        val lInputEmail = lEmail.parent.parent as TextInputLayout

        val email = lEmail.text.toString()
        val password = lPassword.text.toString()

        dbHelper.readableDatabase

        val checkEmail = dbHelper.checkCredentials(email, password)

        if (checkEmail) {
            lInputEmail.isErrorEnabled = false
            Toast.makeText(this, "Session succesfull!", Toast.LENGTH_SHORT).show()
            bestEmail = email
            goToNotes()
        } else {
            lInputEmail.isErrorEnabled = true
            lInputEmail.error = "error the email o password is wrong!"
            Toast.makeText(this, "error the email o password is wrong!", Toast.LENGTH_SHORT).show()
        }


    }

    private fun goToRegister() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    private fun goToNotes() {
        val intent = Intent(this, notes::class.java)
        startActivity(intent)
    }

}