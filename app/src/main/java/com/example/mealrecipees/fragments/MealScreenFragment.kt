package com.example.mealrecipees.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.mealrecipees.R
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.databinding.FragmentMealScreenBinding
import com.example.mealrecipees.databinding.FragmentSplashBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.MealViewModel
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class MealScreenFragment : Fragment() {
    private lateinit var binding: FragmentMealScreenBinding
    private val viewModel by inject<MealViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = MealScreenFragmentArgs.fromBundle(arguments as Bundle)
        viewModel.setMeal(args.meal)
        viewModel.checkMeal()
        viewModel.isLiked()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealScreenBinding.inflate(inflater, container, false)
        binding.mealImageComposeView.setContent {
            val meal by viewModel.meal
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = rememberImagePainter(
                        data = meal.strMealThumb,
                        builder = { transformations(CircleCropTransformation()) }),
                    contentDescription = null,
                    modifier = Modifier.size(220.dp)
                )
            }
        }
        binding.mealTitle.text = viewModel.meal.value.strMeal ?: ""
        binding.choiceListComposeView.setContent {
            val choice by viewModel.choice
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (choice == "ingredients") {
                    viewModel.meal.value.run {
                        ShowText(value = strIngredient1)
                        ShowText(value = strIngredient2)
                        ShowText(value = strIngredient3)
                        ShowText(value = strIngredient4)
                        ShowText(value = strIngredient5)
                        ShowText(value = strIngredient6)
                        ShowText(value = strIngredient7)
                        ShowText(value = strIngredient8)
                        ShowText(value = strIngredient9)
                        ShowText(value = strIngredient10)
                    }
                } else {
                    viewModel.meal.value.run {
                        strInstructions?.split('.')?.let {
                            it.forEachIndexed { i, ins ->
                                if (ins.isNotBlank())
                                    ShowText(value = "${i + 1}. $ins")
                            }
                        }
                    }
                }
            }
        }
        binding.ingredientsTv.setOnClickListener {
            binding.ingredientsTv.setTextColor(requireContext().getColor(R.color.primary))
            binding.ingredientsTv.setBackgroundResource(R.drawable.full_rounded_corner_secondery_color)
            binding.instructionsTv.setTextColor(requireContext().getColor(R.color.secondary))
            binding.instructionsTv.setBackgroundColor(requireContext().getColor(R.color.background))
            viewModel.setChoice("ingredients")
        }
        binding.instructionsTv.setOnClickListener {
            binding.instructionsTv.setTextColor(requireContext().getColor(R.color.primary))
            binding.instructionsTv.setBackgroundResource(R.drawable.full_rounded_corner_secondery_color)
            binding.ingredientsTv.setTextColor(requireContext().getColor(R.color.secondary))
            binding.ingredientsTv.setBackgroundColor(requireContext().getColor(R.color.background))
            viewModel.setChoice("instructions")
        }
        binding.favouriteButton.setOnClickListener {
            viewModel.handleButtonClick()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            viewModel.liked.collect {
                if (it)
                    binding.favouriteButton.setImageResource(R.drawable.ic_favourite_foreground)
                else
                    binding.favouriteButton.setImageResource(R.drawable.ic_not_liked_foreground)
            }
        }
    }

    @Composable
    fun ShowText(value: String?) {
        if (value.isNullOrBlank() || value.isNullOrEmpty())
            return
        else
            Text(
                text = value,
                fontSize = 16.sp,
                color = colorResource(id = R.color.primaryText),
                modifier = Modifier.padding(8.dp)
            )
    }
}