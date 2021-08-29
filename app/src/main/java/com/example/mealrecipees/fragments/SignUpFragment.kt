package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mealrecipees.R
import com.example.mealrecipees.databinding.FragmentSignUpBinding
import com.example.mealrecipees.databinding.FragmentSplashBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.SignUpViewModel
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by inject<SignUpViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.loginBtn.setOnClickListener {
            val username = binding.loginEmailText.text.toString()
            val password = binding.loginPasswordText.text.toString()
            viewModel.signUp(username.trim(), password.trim(), requireContext())
        }
        binding.loginTextNav.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenStarted {
            viewModel.userState.collect {
                when (it) {
                    is NetworkResponse.Success -> findNavController().popBackStack()
                    is NetworkResponse.Loading -> {
                        binding.loginProgress.isVisible = true
                        binding.loginBtn.isVisible = false
                        binding.errorTextView.isVisible = false
                    }
                    is NetworkResponse.Error -> {
                        binding.loginProgress.isVisible = false
                        binding.loginBtn.isVisible = true
                        binding.errorTextView.text = it.error
                        binding.errorTextView.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }
}