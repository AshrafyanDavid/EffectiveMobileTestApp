package com.example.effectivemobiletest.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.effectivemobiletest.presentation.main.fragments.AccountFragment
import com.example.effectivemobiletest.presentation.main.fragments.HotelsFragment
import com.example.effectivemobiletest.presentation.main.fragments.LocationFragment
import com.example.effectivemobiletest.presentation.main.fragments.NotificationFragment
import com.example.effectivemobiletest.presentation.main.fragments.airtickets.AirTicketsFragment

class MainFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AirTicketsFragment.getInstance()
            1 -> HotelsFragment.getInstance()
            2 -> LocationFragment.getInstance()
            3 -> NotificationFragment.getInstance()
            else -> AccountFragment.getInstance()
        }
    }
}
