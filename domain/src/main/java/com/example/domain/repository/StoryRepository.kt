package com.example.domain.repository

import com.example.domain.model.Language
import com.example.domain.model.Story
import com.example.domain.model.StoryTheme
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    suspend fun generateStory(childName: String, theme: StoryTheme, language: Language): Result<Story>
    suspend fun saveStory(story: Story): Result<Unit>
    fun getSavedStories(): Flow<List<Story>>
    suspend fun deleteStory(storyId: String): Result<Unit>
    suspend fun getPromptTemplate(language: Language): String
}
