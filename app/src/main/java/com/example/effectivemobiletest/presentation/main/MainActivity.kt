package com.example.effectivemobiletest.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.effectivemobiletest.R
import com.example.effectivemobiletest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPagerWithBottomNavigation()
    }

    private fun setupViewPagerWithBottomNavigation(): Unit = with(binding) {
        viewPager.apply {
            adapter = MainFragmentStateAdapter(this@MainActivity)
            isUserInputEnabled = false
        }
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.air_tickets -> viewPager.currentItem = 0
                R.id.hotels -> viewPager.currentItem = 1
                R.id.location -> viewPager.currentItem = 2
                R.id.notification -> viewPager.currentItem = 3
                R.id.account -> viewPager.currentItem = 4
            }
            true
        }
    }
}
