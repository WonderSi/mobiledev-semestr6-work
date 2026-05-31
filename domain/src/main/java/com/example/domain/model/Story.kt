package com.example.domain.model

data class Story(
    val id: String,
    val title: String,
    val content: String,
    val theme: StoryTheme,
    val childName: String,
    val language: Language,
    val createdAt: Long
)
