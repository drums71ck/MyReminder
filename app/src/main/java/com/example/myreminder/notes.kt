package com.example.myreminder

import android.media.MediaPlayer
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myreminder.databinding.ActivityNotesBinding


/**
 * Esta clase es la controladora de los fragments
 * de home/dashborad/notification
 *
 */
class notes : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_notes)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mediaPlayer = MediaPlayer.create(this, R.raw.click_sound)

        navView.setOnNavigationItemSelectedListener { menuItem ->
            // Reproducir sonido al presionar un elemento del Navigation Bar
            mediaPlayer.start()

            when (menuItem.itemId) {
                R.id.navigation_home -> {

                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_dashboard -> {

                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_notifications -> {

                    navController.navigate(R.id.navigation_notifications)
                    true
                }
                else -> false
            }
        }
    }
}
