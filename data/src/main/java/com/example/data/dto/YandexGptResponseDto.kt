package com.example.data.dto

data class YandexGptResponseDto(
    val result: ResultDto
)

data class ResultDto(
    val alternatives: List<AlternativeDto>
)

data class AlternativeDto(
    val message: MessageDto
)
