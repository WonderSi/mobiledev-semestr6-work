package com.example.data.firebase

import com.example.domain.model.Language
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteConfigDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate().await()
    }

    fun getPromptTemplate(language: Language): String {
        val key = when (language) {
            Language.RU -> "yandex_prompt_ru"
            Language.EN -> "yandex_prompt_en"
        }
        return remoteConfig.getString(key).ifEmpty { defaultPrompt(language) }
    }

    private fun defaultPrompt(language: Language): String = when (language) {
        Language.RU -> "Напиши сказку на ночь для ребёнка по имени {name} на тему \"{theme}\". " +
                "Сказка должна быть доброй, с моралью, длиной 5-7 абзацев. " +
                "Начни с названия сказки на первой строке."
        Language.EN -> "Write a bedtime story for a child named {name} about \"{theme}\". " +
                "The story should be kind, with a moral, 5-7 paragraphs long. " +
                "Start with the story title on the first line."
    }
}
