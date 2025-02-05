package com.example.domain.repo

import com.example.domain.entity.CategoryResponse
import retrofit2.Response

interface MealsRepo {
    suspend fun gatMealsFromRemote(): Response<CategoryResponse>
}