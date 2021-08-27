package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mealrecipees.databinding.FragmentListsBinding
import com.example.mealrecipees.databinding.FragmentSplashBinding

class ListsFragment : Fragment() {
    private lateinit var binding: FragmentListsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }
}