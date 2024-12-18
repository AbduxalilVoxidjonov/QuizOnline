package com.example.quizonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.quizonline.Fragment.HomeFragment
import com.example.quizonline.Fragment.ProfileFragment
import com.example.quizonline.Fragment.ScoreFragment
import com.example.quizonline.databinding.ActivityMainBinding
import com.google.android.material.color.utilities.Score

class MyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

}