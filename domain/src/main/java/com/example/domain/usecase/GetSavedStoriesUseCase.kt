package com.example.domain.usecase

import com.example.domain.model.Story
import com.example.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedStoriesUseCase @Inject constructor(
    private val repository: StoryRepository
) {
    operator fun invoke(): Flow<List<Story>> =
        repository.getSavedStories()
}
