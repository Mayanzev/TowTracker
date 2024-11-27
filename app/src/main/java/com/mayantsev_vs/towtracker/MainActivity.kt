package com.mayantsev_vs.towtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mayantsev_vs.towtracker.databinding.ActivityMainBinding
import com.mayantsev_vs.towtracker.fragments.MainFragment
import com.mayantsev_vs.towtracker.fragments.SettingsFragment
import com.mayantsev_vs.towtracker.fragments.TracksFragment
import com.mayantsev_vs.towtracker.utils.openFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBottomNavClick()
        openFragment(MainFragment.newInstance())
    }

    private fun onBottomNavClick() {
        binding.bNan.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> openFragment(MainFragment.newInstance())
                R.id.id_tracks -> openFragment(TracksFragment.newInstance())
                R.id.id_settings -> openFragment(SettingsFragment())
            }
            true
        }
    }

}