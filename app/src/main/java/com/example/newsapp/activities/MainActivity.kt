package com.example.newsapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.fragments.NewsFragment
import com.example.newsapp.fragments.ProfileFragment
import com.example.newsapp.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = resources.getColor(R.color.black)

        val firstFragment=NewsFragment()
        val secondFragment=SearchFragment()
        val thirdFragment=ProfileFragment()

        setCurrentFragment(firstFragment)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.itemActiveIndicatorColor = null
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home->setCurrentFragment(firstFragment)
                R.id.search->setCurrentFragment(secondFragment)
                R.id.profile->setCurrentFragment(thirdFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment:Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentReplace, fragment)
        transaction.commit()
    }
}
