package com.example.domain.usecase

import com.example.domain.model.Language
import com.example.domain.model.Story
import com.example.domain.model.StoryTheme
import com.example.domain.repository.StoryRepository
import javax.inject.Inject

class GenerateStoryUseCase @Inject constructor(
    private val repository: StoryRepository
) {
    suspend operator fun invoke(
        childName: String,
        theme: StoryTheme,
        language: Language
    ): Result<Story> = repository.generateStory(childName, theme, language)
}
