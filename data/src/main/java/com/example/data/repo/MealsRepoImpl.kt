package com.example.data.repo

import com.example.data.remote.ApiService
import com.example.domain.entity.CategoryResponse
import com.example.domain.repo.MealsRepo
import retrofit2.Response

class MealsRepoImpl(private val apiService: ApiService) : MealsRepo {

    // return Response<CategoryResponse>
    override suspend fun gatMealsFromRemote() = apiService.getMeals()

}