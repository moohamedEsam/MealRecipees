package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mealrecipees.databinding.FragmentMealScreenBinding
import com.example.mealrecipees.databinding.FragmentSplashBinding

class MealScreenFragment : Fragment() {
    private lateinit var binding: FragmentMealScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
}