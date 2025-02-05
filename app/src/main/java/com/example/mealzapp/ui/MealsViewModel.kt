package com.example.mealzapp.ui


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.CategoryResponse
import com.example.domain.usecase.GetMeals
import com.example.mealzapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val app: Application,
    private val getMealsUseCase: GetMeals
) : AndroidViewModel(app) {

//    private val _mealsStateFlow = MutableStateFlow<CategoryResponse?>(null)
//    val mealsStateFlow: StateFlow<CategoryResponse?> get() = _mealsStateFlow


//    fun getMeals() = viewModelScope.launch {
//        try {
//            // this direct call object and call function automatic cause( operator fun invoke() )
//            _mealsStateFlow.value = getMealsUseCase()
//        } catch (e: Exception) {
//            Log.e("MealsViewModel", e.message.toString())
//        }
//    }


    private val _mealsStateFlow = MutableStateFlow<Resource<CategoryResponse>?>(null)
    val mealsStateFlow: StateFlow<Resource<CategoryResponse>?> get() = _mealsStateFlow


    fun getMeals() = viewModelScope.launch {
        _mealsStateFlow.value = Resource.Loading
        try {
            if (isInternetAvailable()) {
                // this direct call object and call function automatic cause( operator fun invoke() )
                val response = getMealsUseCase()
                _mealsStateFlow.value = handelMealsResponse(response)
            } else {
                _mealsStateFlow.value = Resource.Failure("No internet connection")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _mealsStateFlow.value = Resource.Failure("Network failure")
                else -> _mealsStateFlow.value = Resource.Failure("Conversion Error")
            }
        }

    }


    private fun handelMealsResponse(response: Response<CategoryResponse>): Resource<CategoryResponse> {
        // if data return and is good
        if (response.isSuccessful) {
            response.body()?.let { resultResponse  ->
                return Resource.Success(resultResponse  )
            }
        }
        // if data not return
        return Resource.Failure(response.message())
    }


    fun isInternetAvailable(): Boolean {
        val context = getApplication<Application>().applicationContext

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}