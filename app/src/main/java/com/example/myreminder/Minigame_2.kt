package com.example.myreminder

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import java.io.IOException
import java.util.Random

class Minigame_2 : AppCompatActivity() {


    private  val buttonsIds = arrayOf(
        R.id.boton00, R.id.boton01, R.id.boton02, R.id.boton03,
        R.id.boton04, R.id.boton05, R.id.boton06, R.id.boton07,
        R.id.boton08, R.id.boton09, R.id.boton10, R.id.boton11,
        R.id.boton12, R.id.boton13, R.id.boton14, R.id.boton15,
        R.id.boton15, R.id.boton16, R.id.boton17, R.id.boton18,
        R.id.boton19, R.id.boton20, R.id.boton21, R.id.boton22,
        R.id.boton23,
    )
    private val soundIds = arrayOf(
        R.raw.bubble1, R.raw.bubble2, R.raw.bubble3
    )
    private var bubblesCount = buttonsIds.size
    private lateinit var mediaPlayers : Array<MediaPlayer>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minigame2)
        supportActionBar?.hide()

        mediaPlayers = Array(soundIds.size) { MediaPlayer() }

        for (buttonId in buttonsIds){
            val button: ImageButton = findViewById(buttonId)
            button.setBackgroundResource(R.drawable.bubble)
            button.setOnClickListener(){onBubbleClicked(button)}
        }
    }

    /**
     * Esta función estara encarga de controlar
     * las burbujas presionadas y darles un sonido
     *
     * @param button
     */
    private fun onBubbleClicked(button: ImageButton) {
        button.setBackgroundResource(R.drawable.bubble_crash)
        button.isClickable = false
        bubblesCount--

        // Reproducir sonido de burbuja aleatorio
        val randomSoundId = if (Random().nextInt(100) < 2) {
            // Probabilidad del 2% de reproducir el sonido meow
            R.raw.meow
        } else {
            soundIds.random()
        }

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setOnCompletionListener { mp ->
            mp.release()
        }

        try {
            mediaPlayer.setDataSource(resources.openRawResourceFd(randomSoundId))
            mediaPlayer.prepare()

            if (randomSoundId == R.raw.meow) {
                // Bajar el volumen del sonido "meow" a la mitad
                mediaPlayer.setVolume(0.3f, 0.3f)
            }

            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
            // Manejar la excepción de manera adecuada
        }
        if (bubblesCount == 1) {
            restartGame()
        }
    }

    /**
     * Lo que hace esta función es sencillo
     * reiniciara el juego voliendo cargar
     * la imagen de la burbuja
     *
     */
    private fun restartGame() {
        for (buttonId in buttonsIds){
            val button: ImageButton = findViewById(buttonId)
            button.setBackgroundResource(R.drawable.bubble)
            button.isClickable = true
        }
        bubblesCount = buttonsIds.size
    }


}