package com.example.myreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    lateinit var btnRegister : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cargarBtn()

    }
    fun cargarBtn(){
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener(){
            goToRegister()
        }

    }

    private fun goToRegister() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }
}