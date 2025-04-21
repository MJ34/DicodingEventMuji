package com.rsjd.dicodingeventmuji.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rsjd.dicodingeventmuji.R
import com.rsjd.dicodingeventmuji.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_upcoming -> {
                    navController.navigate(R.id.navigation_upcoming)
                    true
                }
                R.id.navigation_finished -> {
                    navController.navigate(R.id.navigation_finished)
                    true
                }
                else -> false
            }
        }
    }
}