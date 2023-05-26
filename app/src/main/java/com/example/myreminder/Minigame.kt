package com.example.myreminder

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
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
import android.widget.Switch
import androidx.annotation.RequiresApi
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.myreminder.ui.notifications.NotificationsFragment

/**
 * @author drums71ck
 * Esta clase sera nuestro controlador para los minijuegos
 */
class Minigame : AppCompatActivity() {

    // Definimos las variables

    private lateinit var buttonTriangle: ImageButton
    private lateinit var buttonCircle: ImageButton
    private lateinit var buttonSquare: ImageButton
    private lateinit var buttonX: ImageButton
    private lateinit var counterTextView: TextView
    private lateinit var btnSwOn: ImageButton
    private lateinit var btnSwOff: ImageButton
    private var mediaPlayerSwOn: MediaPlayer? = null
    private var mediaPlayerSwOff: MediaPlayer? = null

    val handler = Handler()
    var isButtonPressed = false


    private var counter = 0

    /**
     * aca inicializaremos todos los botones
     * y musica del minigame
     *
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minigame)

        // definimos las variables
        supportActionBar?.hide()
        btnSwOff = findViewById(R.id.btnSwOff)
        btnSwOn = findViewById(R.id.btnSwOn)
        buttonTriangle = findViewById(R.id.button_triangle)
        buttonCircle = findViewById(R.id.button_circle)
        buttonSquare = findViewById(R.id.button_square)
        buttonX = findViewById(R.id.button_x)
        counterTextView = findViewById(R.id.counterTextView)

        mediaPlayerSwOn = MediaPlayer.create(this, R.raw.sw_on)
        mediaPlayerSwOff = MediaPlayer.create(this, R.raw.sw_off)

        // Mandamos cada boton a la función bigButton
        bigButton(buttonX)
        bigButton(buttonCircle)
        bigButton(buttonTriangle)
        bigButton(buttonSquare)

        // Cambiara de visibilidad cada que se presionen los botones
        btnSwOn.isVisible = false
        btnSwOff.setOnClickListener(){
            btnSwOn.isVisible = true
            btnSwOff.isVisible = false
            mediaPlayerSwOff?.start()
        }
        btnSwOn.setOnClickListener(){
            btnSwOff.isVisible = true
            btnSwOn.isVisible = false
            mediaPlayerSwOn?.start()
        }

    }


    val repeatAction = object : Runnable {
        override fun run() {
            if (isButtonPressed) {
                incrementCounter()
                handler.postDelayed(this, 100) // Intervalo de repetición en milisegundos
            }
        }
    }

    /**
     *Esta función incrementara el texView de los botones presionados
     *ademas de agregar vibracion como feedback al usuario
     *
     */
    private fun activeVibrate(){
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
    private fun incrementCounter() {
        counter++
        counterTextView.text = counter.toString()
            if (!NotificationsFragment.flagVibration){
                activeVibrate()
            }else{
                desactiveVibrate()
            }




    }

    private fun desactiveVibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Detener cualquier vibración en curso
        vibrator.cancel()    }

    /**
     * Esta función simplemente hara que los botones dupliquen
     * su tamaño, en el tiempo que este presionados
     *
     * @param button cada boton presionado
     *
     */
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

    /**
     * Esta función se encargara de destruir el mp3
     * solo por optimización
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerSwOn?.release()
        mediaPlayerSwOff?.release()
    }

}
