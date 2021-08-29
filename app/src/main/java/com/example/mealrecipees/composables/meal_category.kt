package com.example.mealrecipees.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.mealrecipees.R
import com.example.mealrecipees.dataModels.Category
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.MainScreenViewModel


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
 fun MealComposable(
    viewModel: MainScreenViewModel,
    onClick: (Meal) -> Unit
) {
    val meals by viewModel.mealsState
    when (meals) {
        is NetworkResponse.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(id = R.color.primary))
            }
        }
        is NetworkResponse.Success -> {
            MealsGridList(viewModel.meals, onClick){
                viewModel.setMealsState()
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

@ExperimentalFoundationApi
@Composable
fun MealsGridList(
    meals: List<Meal>,
    onClick: (Meal) -> Unit,
    paginate: () -> Unit
) {
    LazyVerticalGrid(cells = GridCells.Adaptive(120.dp)) {
        itemsIndexed(meals) { i, meal ->
            if (i == meals.size - 1)
                paginate()
            MealListItem(meal = meal) {
                onClick(meal)
            }
        }
    }
}

@Composable
 fun CategoryComposable(
    viewModel: MainScreenViewModel,
    onClick: (Category) -> Unit
) {
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
                                onClick(cat)
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
@Composable
fun MealListItem(meal: Meal, onClick: (Meal) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth(0.48f)
        .clickable {
            onClick(meal)
        }
    ) {
        Image(
            painter = rememberImagePainter(
                data = meal.strMealThumb ?: "",
                builder = { transformations(CircleCropTransformation()) }),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
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
