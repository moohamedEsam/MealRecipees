package com.example.mealrecipees.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mealrecipees.R
import com.example.mealrecipees.composables.MealsGridList
import com.example.mealrecipees.dataModels.Category
import com.example.mealrecipees.databinding.FragmentCategoryBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.CategoryViewModel
import org.koin.android.ext.android.inject

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: CategoryViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setCategoryState()
    }
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        binding.categoryComposeView.setContent {
            val category by viewModel.categoryState
            when (category) {
                is NetworkResponse.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(id = R.color.primary))
                    }
                }
                is NetworkResponse.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        category.data?.categories?.let {
                            items(it) { cat ->
                                CategoryListItem(category = cat) {name ->
                                    val action =
                                        CategoryFragmentDirections.actionCategoryFragmentToCategoryMealsFragment(name)
                                    findNavController().navigate(action)
                                }
                            }
                        }
                    }

                }
                is NetworkResponse.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = category.error ?: "",
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.red)
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
    fun CategoryListItem(category: Category, onClick: (String) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick(category.strCategory ?: "") },
            backgroundColor = colorResource(id = R.color.secondary)
        ) {
            Row {
                Image(
                    painter = rememberImagePainter(data = category.strCategoryThumb ?: ""),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = category.strCategory ?: "",
                        color = colorResource(id = R.color.primaryText),
                        maxLines = 1,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = category.strCategoryDescription ?: "",
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