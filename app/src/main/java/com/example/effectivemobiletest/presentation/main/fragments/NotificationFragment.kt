package com.example.effectivemobiletest.presentation.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.effectivemobiletest.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    companion object {
        fun getInstance() = NotificationFragment()
    }

    private var binding: FragmentNotificationBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}