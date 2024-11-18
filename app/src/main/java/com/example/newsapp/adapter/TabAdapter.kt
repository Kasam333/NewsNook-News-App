package com.example.newsapp.adapter

import com.example.newsapp.fragments.BusinessFragment
import com.example.newsapp.fragments.EntertainmentFragment
import com.example.newsapp.fragments.HealthFragment
import com.example.newsapp.fragments.ScienceFragment
import com.example.newsapp.fragments.SportsFragment
import com.example.newsapp.fragments.TechnologyFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 6 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BusinessFragment()
            1 -> SportsFragment()
            2 -> TechnologyFragment()
            3 -> HealthFragment()
            4 -> ScienceFragment()
            else -> EntertainmentFragment()
        }
    }
}

