package com.example.data.remote

import com.example.data.dto.YandexGptRequestDto
import com.example.data.dto.YandexGptResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface YandexGptApi {
    @POST("foundationModels/v1/completion")
    suspend fun generateText(
        @Header("Authorization") authorization: String,
        @Body request: YandexGptRequestDto
    ): YandexGptResponseDto
}
