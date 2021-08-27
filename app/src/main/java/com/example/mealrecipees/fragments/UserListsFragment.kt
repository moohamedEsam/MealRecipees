package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mealrecipees.databinding.FragmentSplashBinding
import com.example.mealrecipees.databinding.FragmentUserListsBinding

class UserListsFragment : Fragment() {
    private lateinit var binding: FragmentUserListsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListsBinding.inflate(inflater, container, false)
        return binding.root
    }
}