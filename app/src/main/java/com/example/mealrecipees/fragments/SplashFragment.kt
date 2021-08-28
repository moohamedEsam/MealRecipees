package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mealrecipees.R
import com.example.mealrecipees.databinding.FragmentSplashBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            delay(500)
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser == null)
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            else
                findNavController().navigate(R.id.action_splashFragment_to_mainScreenFragment)
        }
    }
}