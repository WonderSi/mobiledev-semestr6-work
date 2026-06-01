package com.example.data.dto

data class StoryDto(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val theme: String = "",
    val childName: String = "",
    val language: String = "",
    val createdAt: Long = 0L
)
