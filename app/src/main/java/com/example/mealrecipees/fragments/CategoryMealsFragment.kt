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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mealrecipees.R
import com.example.mealrecipees.composables.MealsGridList
import com.example.mealrecipees.databinding.FragmentCategoryMealsBinding
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.viewModels.CategoryMealsViewModel
import org.koin.android.ext.android.inject

class CategoryMealsFragment : Fragment() {
    private lateinit var binding: FragmentCategoryMealsBinding
    private val viewModel: CategoryMealsViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = CategoryMealsFragmentArgs.fromBundle(arguments as Bundle)
        viewModel.category = args.category
        viewModel.setCategoryState()
    }


    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryMealsBinding.inflate(inflater, container, false)
        binding.categoryTitle.text = viewModel.category
        binding.categoryMealsComposeView.setContent {
            ComposeList()
        }
        return binding.root
    }

    @ExperimentalFoundationApi
    @Composable
    private fun ComposeList() {
        val category by viewModel.categoryState
        when (category) {
            is NetworkResponse.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(id = R.color.primary))
                }
            }
            is NetworkResponse.Success -> {
                category.data?.meals?.let {
                    MealsGridList(
                        it,
                        { meal ->
                            val action =
                                CategoryMealsFragmentDirections.actionCategoryMealsFragmentToMealScreenFragment(
                                    meal
                                )
                            findNavController().navigate(action)
                        }
                    ) {

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
}