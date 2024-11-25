package com.example.towtracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.towtracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBottomNavClick()
    }

    private fun onBottomNavClick() {
        binding.bNan.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                R.id.id_tracks -> Toast.makeText(this, "Tracks", Toast.LENGTH_SHORT).show()
                R.id.id_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

}