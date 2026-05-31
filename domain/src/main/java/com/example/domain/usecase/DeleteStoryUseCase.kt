package com.example.domain.usecase

import com.example.domain.repository.StoryRepository
import javax.inject.Inject

class DeleteStoryUseCase @Inject constructor(
    private val repository: StoryRepository
) {
    suspend operator fun invoke(storyId: String): Result<Unit> =
        repository.deleteStory(storyId)
}
