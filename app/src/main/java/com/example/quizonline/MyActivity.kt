package com.example.quizonline

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.quizonline.Fragment.HomeFragment
import com.example.quizonline.Fragment.ProfileFragment
import com.example.quizonline.Fragment.ScoreFragment
import com.example.quizonline.databinding.ActivityMainBinding

class MyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        removePref()

        replaceFragment(HomeFragment())

        binding.toolbar.title = "Home"

        binding.apply {

            bottomNavigation.setOnItemSelectedListener {

                when (it.itemId) {

                    R.id.bottom_home -> {
                        replaceFragment(HomeFragment())
                        toolbar.title = "Home"
                    }

                    R.id.bottom_score -> {
                        replaceFragment(ScoreFragment())
                        toolbar.title = "Score"
                    }

                    R.id.bottom_profile -> {
                        replaceFragment(ProfileFragment())
                        toolbar.title = "Profile"
                    }
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    // ondestroy is removed prefeenses
//    fun removePref() {
//        val sharedPref = getSharedPreferences("last_login", MODE_PRIVATE)
//        val lastLoginTime = sharedPref.getLong("time", 0L)
//
//        // Check if the time difference is 86400 seconds (1 day) in milliseconds
//        val oneDayInMillis = 86400000L
//        if ((System.currentTimeMillis() - lastLoginTime) >= oneDayInMillis) {
//            // Clear preferences if the time difference is 1 day or more
//            getSharedPreferences("app_prefs", MODE_PRIVATE).edit().clear().apply()
//
//        }
//    }



}