package com.example.data.repository

import com.example.data.dto.StoryDto
import com.example.data.firebase.FirestoreDataSource
import com.example.data.firebase.RemoteConfigDataSource
import com.example.data.remote.YandexGptDataSource
import com.example.domain.model.Language
import com.example.domain.model.Story
import com.example.domain.model.StoryTheme
import com.example.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val yandexGptDataSource: YandexGptDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val remoteConfigDataSource: RemoteConfigDataSource
) : StoryRepository {

    override suspend fun generateStory(
        childName: String,
        theme: StoryTheme,
        language: Language
    ): Result<Story> = runCatching {
        val prompt = remoteConfigDataSource.getPromptTemplate(language)
            .replace("{name}", childName)
            .replace("{theme}", theme.getLabel(language))

        val text = yandexGptDataSource.generateStory(prompt)
        val lines = text.trim().lines()
        val title = lines.firstOrNull()?.trim() ?: ""
        val content = lines.drop(1).joinToString("\n").trim()

        Story(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            theme = theme,
            childName = childName,
            language = language,
            createdAt = System.currentTimeMillis()
        )
    }

    override suspend fun saveStory(story: Story): Result<Unit> =
        firestoreDataSource.saveStory(story.toDto())

    override fun getSavedStories(): Flow<List<Story>> =
        firestoreDataSource.getStories().map { dtos ->
            dtos.mapNotNull { it.toDomain() }
        }

    override suspend fun deleteStory(storyId: String): Result<Unit> =
        firestoreDataSource.deleteStory(storyId)

    override suspend fun getPromptTemplate(language: Language): String =
        remoteConfigDataSource.getPromptTemplate(language)
}

private fun Story.toDto() = StoryDto(
    id = id,
    title = title,
    content = content,
    theme = theme.name,
    childName = childName,
    language = language.name,
    createdAt = createdAt
)

private fun StoryDto.toDomain(): Story? = runCatching {
    Story(
        id = id,
        title = title,
        content = content,
        theme = StoryTheme.valueOf(theme),
        childName = childName,
        language = Language.valueOf(language),
        createdAt = createdAt
    )
}.getOrNull()

private fun StoryTheme.getLabel(language: Language): String = when (language) {
    Language.RU -> labelRu
    Language.EN -> labelEn
}
