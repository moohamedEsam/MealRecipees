package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mealrecipees.R
import com.example.mealrecipees.databinding.FragmentProfileBinding
import com.example.mealrecipees.viewModels.LoginViewModel
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by inject<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.logout.setOnClickListener {
            if (viewModel.logOut(requireContext()))
                findNavController().navigate(R.id.action_global_loginFragment)
        }
        return binding.root
    }
}