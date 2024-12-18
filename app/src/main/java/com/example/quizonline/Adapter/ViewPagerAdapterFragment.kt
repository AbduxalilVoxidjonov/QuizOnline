package com.example.quizonline.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.quizonline.Fragment.HomeFragment
import com.example.quizonline.Fragment.ProfileFragment
import com.example.quizonline.Fragment.ScoreFragment


class ViewPagerAdapterFragment(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            HomeFragment()
        } else if (position == 2) {
            ScoreFragment()
        } else {
            ProfileFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}