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
import com.example.mealrecipees.dataModels.Meal
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
            CategoryComposable()
        }
        binding.mealsComposeView.setContent {
            MealComposable()
        }
        return binding.root
    }

    @Composable
    private fun CategoryComposable() {
        val categories by viewModel.categoryState
        when (categories) {
            is NetworkResponse.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(id = R.color.primary))
                }
            }
            is NetworkResponse.Success -> {
                categories.data?.categories?.let {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(it) { cat ->
                            OutlinedButton(
                                onClick = {
                                    val action =
                                        MainScreenFragmentDirections
                                            .actionMainScreenFragmentToCategoryFragment((cat))
                                    findNavController().navigate(action)
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = cat.strCategory ?: "",
                                    color = colorResource(id = R.color.primary)
                                )
                            }
                        }
                    }
                }

            }
            is NetworkResponse.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = categories.error ?: "",
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.red)
                    )
                }
            }

            else -> Unit
        }
    }

    @ExperimentalCoilApi
    @ExperimentalFoundationApi
    @Composable
    private fun MealComposable() {
        val meals by viewModel.mealsState
        when (meals) {
            is NetworkResponse.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(id = R.color.primary))
                }
            }
            is NetworkResponse.Success -> {
                LazyVerticalGrid(cells = GridCells.Adaptive(120.dp)) {
                    itemsIndexed(viewModel.meals) { i, meal ->
                        if (i == viewModel.meals.size - 1)
                            viewModel.setMealsState()
                        MealListItem(meal = meal)
                    }
                }

            }
            is NetworkResponse.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = meals.error ?: "",
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.red)
                    )
                }
            }

            else -> Unit
        }
    }

    @ExperimentalCoilApi
    @Composable
    private fun MealListItem(meal: Meal) {
        Column(modifier = Modifier
            .fillMaxWidth(0.48f)
            .clickable {
                val action =
                    MainScreenFragmentDirections.actionMainScreenFragmentToMealScreenFragment(
                        meal
                    )
                findNavController().navigate(action)
            }
        ) {
            Image(
                painter = rememberImagePainter(
                    data = meal.strMealThumb ?: "",
                    builder = { transformations(CircleCropTransformation()) }),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
                    .padding(8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                backgroundColor = colorResource(id = R.color.secondary)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = meal.strMeal ?: "",
                        color = colorResource(id = R.color.primaryText),
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                    Text(text = meal.strCategory ?: "", color = colorResource(id = R.color.primary))
                    Text(
                        text = meal.strTags?.replace(',', '\t') ?: "",
                        color = colorResource(id = R.color.primary),
                        maxLines = 1
                    )
                }


            }
        }
    }
}