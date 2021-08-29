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
import com.example.mealrecipees.databinding.FragmentLoginBinding
import com.example.mealrecipees.databinding.FragmentSplashBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.LoginViewModel
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by inject<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginBtn.setOnClickListener {
            val username = binding.loginEmailText.text.toString()
            val password = binding.loginPasswordText.text.toString()
            viewModel.signIn(username.trim(), password.trim())
        }
        binding.registerTextNav.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            viewModel.userStatus.collect {
                when (it) {
                    is NetworkResponse.Success -> findNavController().navigate(R.id.action_loginFragment_to_mainScreenFragment)
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