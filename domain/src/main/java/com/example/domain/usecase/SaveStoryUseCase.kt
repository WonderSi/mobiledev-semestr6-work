package com.example.domain.usecase

import com.example.domain.model.Story
import com.example.domain.repository.StoryRepository
import javax.inject.Inject

class SaveStoryUseCase @Inject constructor(
    private val repository: StoryRepository
) {
    suspend operator fun invoke(story: Story): Result<Unit> =
        repository.saveStory(story)
}
