package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mealrecipees.R
import com.example.mealrecipees.composables.MealsGridList
import com.example.mealrecipees.databinding.FragmentUserLikesBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.UserLikesViewModel
import org.koin.android.ext.android.inject

class UserLikesFragment : Fragment() {
    private lateinit var binding: FragmentUserLikesBinding
    private val viewModel by inject<UserLikesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUserLikes()
    }


    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserLikesBinding.inflate(inflater, container, false)
        binding.likedMealsComposeView.setContent {
            val meals by viewModel.userLikes
            when (meals) {
                is NetworkResponse.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(id = R.color.primary))
                    }
                }
                is NetworkResponse.Success -> {
                    meals.data?.let {
                        MealsGridList(
                            meals = it,
                            onClick = { meal ->
                                val action =
                                    UserLikesFragmentDirections.actionUserLikesFragmentToMealScreenFragment(
                                        meal
                                    )
                                findNavController().navigate(action)
                            }
                        ) {}
                    }
                }

                is NetworkResponse.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = meals.error ?: "",
                            color = colorResource(id = R.color.red),
                            fontSize = 24.sp
                        )
                    }
                }
                else -> Unit
            }
        }
        return binding.root
    }
}