package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.mealrecipees.R
import com.example.mealrecipees.composables.CategoryComposable
import com.example.mealrecipees.composables.MealComposable
import com.example.mealrecipees.dataModels.Category
import com.example.mealrecipees.dataModels.CategoryResponse
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.databinding.FragmentMainScreenBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.MainScreenViewModel
import org.koin.android.ext.android.inject

class MainScreenFragment : Fragment() {
    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel by inject<MainScreenViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        binding.categoryComposeViewMainScreen.setContent {
            CategoryComposable(viewModel) {
                val action =
                    MainScreenFragmentDirections.actionMainScreenFragmentToCategoryMealsFragment(
                        it.strCategory ?: ""
                    )
                findNavController().navigate(action)
            }
        }
        binding.mealsComposeView.setContent {
            MealComposable(viewModel) {
                val action =
                    MainScreenFragmentDirections.actionMainScreenFragmentToMealScreenFragment(it)
                findNavController().navigate(action)
            }
        }
        binding.moreBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_categoryFragment)
        }
        binding.moreTv.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_categoryFragment)
        }
        return binding.root
    }
}

