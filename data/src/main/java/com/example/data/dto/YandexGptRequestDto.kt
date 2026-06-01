package com.example.data.dto

data class YandexGptRequestDto(
    val modelUri: String,
    val completionOptions: CompletionOptionsDto,
    val messages: List<MessageDto>
)

data class CompletionOptionsDto(
    val stream: Boolean = false,
    val temperature: Double = 0.7,
    val maxTokens: String = "2000"
)

data class MessageDto(
    val role: String,
    val text: String
)
