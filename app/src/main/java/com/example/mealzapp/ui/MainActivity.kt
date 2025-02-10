package com.example.mealzapp.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mealzapp.R
import com.example.mealzapp.dapters.MealsAdapter
import com.example.mealzapp.databinding.ActivityMainBinding
import com.example.mealzapp.utils.Resource
import com.example.mealzapp.utils.hide
import com.example.mealzapp.utils.show
import com.example.mealzapp.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // this dagger hilt get automatic instance from mealsViewModel
    private val viewModel: MealsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }


        viewModel.getMeals()
        val mealsAdapter = MealsAdapter()

//        lifecycleScope.launch {
//            viewModel.mealsStateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect { categoryResponse ->
//                    mealsAdapter.submitList(categoryResponse?.categories)
//                    binding.rvMeals.adapter = mealsAdapter
//                    if (categoryResponse?.categories?.size == 0) {
//                        binding.progressBar.visibility = View.VISIBLE
//                    }else{
//                        binding.progressBar.visibility = View.GONE
//                    }
//
//                }
//        }


        lifecycleScope.launch {
            viewModel.mealsStateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            binding.progressBar.hide()
                            response.data.let { categoryResponse ->
                                mealsAdapter.submitList(categoryResponse.categories)
                                binding.rvMeals.adapter = mealsAdapter
                            }
                        }

                        is Resource.Failure -> {
                            binding.progressBar.hide()
                            response.errorMessage?.let { message ->
                                toast("An Error Occured: $message")
                            }
                        }

                        is Resource.Loading -> {
                            binding.progressBar.show()
                        }

                        else -> {}
                    }
                }
        }



    }


}