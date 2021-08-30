package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mealrecipees.R
import com.example.mealrecipees.composables.MealsGridList
import com.example.mealrecipees.dataModels.Category
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.databinding.FragmentSearchBinding
import com.example.mealrecipees.databinding.FragmentSplashBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.SearchViewModel
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by inject<SearchViewModel>()
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchBtn.setOnClickListener {
            binding.resultTextView.isVisible = true
            val query = binding.searchTextInputText.text.toString().trim()
            when (binding.spinner.selectedItem) {
                "first letter" -> {
                    viewModel.setSearchResult(1, query)
                }
                "category" -> {
                    viewModel.setSearchResult(2, query)
                }
                "main ingredient" -> {
                    viewModel.setSearchResult(3, query)
                }
                else -> { //meal name
                    viewModel.setSearchResult(4, query)
                }
            }
        }
        binding.searchResultListComposeView.setContent {
            val result by viewModel.searchResult
            when (result) {
                is NetworkResponse.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(id = R.color.primary))
                    }
                }
                is NetworkResponse.Success -> {
                    result.data?.let {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            result.data?.meals?.let {
                                items(it) { meal ->
                                    ResultListItem(meal = meal) {
                                        val action =
                                            SearchFragmentDirections.actionSearchFragmentToMealScreenFragment(
                                                meal
                                            )
                                        findNavController().navigate(action)
                                    }
                                }
                            }
                        }
                    }
                }

                is NetworkResponse.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = result.error ?: "",
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

    @ExperimentalCoilApi
    @Composable
    fun ResultListItem(meal: Meal, onClick: (Meal) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick(meal) },
            backgroundColor = colorResource(id = R.color.secondary)
        ) {
            Row {
                Image(
                    painter = rememberImagePainter(data = meal.strMealThumb ?: ""),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = meal.strMeal ?: "",
                        color = colorResource(id = R.color.primaryText),
                        maxLines = 1,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = meal.strCategory ?: "",
                        color = colorResource(id = R.color.secondaryText),
                        fontSize = 14.sp,
                        maxLines = 3,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}