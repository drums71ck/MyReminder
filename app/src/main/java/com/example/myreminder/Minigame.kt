package com.example.myreminder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.*
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import android.widget.TextView
import android.widget.Toast

class Minigame : AppCompatActivity() {
    private lateinit var joystick: View
    private lateinit var buttonTriangle: ImageButton
    private lateinit var buttonCircle: ImageButton
    private lateinit var buttonSquare: ImageButton
    private lateinit var buttonX: ImageButton
    private lateinit var counterTextView: TextView
    val handler = Handler()
    var isButtonPressed = false


    private var counter = 0
    val repeatAction = object : Runnable {
        override fun run() {
            if (isButtonPressed) {
                incrementCounter()
                handler.postDelayed(this, 100) // Intervalo de repetición en milisegundos
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minigame)

        supportActionBar?.hide()
        buttonTriangle = findViewById(R.id.button_triangle)
        buttonCircle = findViewById(R.id.button_circle)
        buttonSquare = findViewById(R.id.button_square)
        buttonX = findViewById(R.id.button_x)
        counterTextView = findViewById(R.id.counterTextView)

        bigButton(buttonX)
        bigButton(buttonCircle)
        bigButton(buttonTriangle)
        bigButton(buttonSquare)

    }

    private fun incrementCounter() {
        counter++
        counterTextView.text = counter.toString()
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect: VibrationEffect = (VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))

                vibrator.vibrate(effect)// New vibrate method for API Level 26 or higher
            } else {
                vibrator.vibrate(500) // Vibrate method for below API Level 26
            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    fun bigButton(button: ImageButton) {
        val originalScaleX = button.scaleX
        val originalScaleY = button.scaleY

        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // El botón ha sido presionado
                    incrementCounter()
                    button.scaleX = originalScaleX * 2 // Aumenta el tamaño en el eje X
                    button.scaleY = originalScaleY * 2 // Aumenta el tamaño en el eje Y
                    true // Indica que se ha manejado el evento
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // El botón ha sido liberado o el evento ha sido cancelado
                    button.scaleX = originalScaleX // Restaura el tamaño original en el eje X
                    button.scaleY = originalScaleY // Restaura el tamaño original en el eje Y
                    true
                }
                else -> false // Manejo de otros eventos táctiles si es necesario
            }
        }
    }

}
