package com.example.data.remote

import com.example.data.dto.CompletionOptionsDto
import com.example.data.dto.MessageDto
import com.example.data.dto.YandexGptRequestDto
import com.example.data.util.Constants
import javax.inject.Inject

class YandexGptDataSource @Inject constructor(
    private val api: YandexGptApi
) {
    suspend fun generateStory(prompt: String): String {
        val request = YandexGptRequestDto(
            modelUri = "gpt://${Constants.YANDEX_FOLDER_ID}/yandexgpt-lite",
            completionOptions = CompletionOptionsDto(),
            messages = listOf(MessageDto(role = "user", text = prompt))
        )
        val response = api.generateText(
            authorization = "Api-Key ${Constants.YANDEX_API_KEY}",
            request = request
        )
        return response.result.alternatives.first().message.text
    }
}
