package com.example.storytime.util

import com.example.domain.model.Language
import com.example.domain.model.StoryTheme
import com.example.storytime.BuildConfig

/** Язык приложения, заданный текущим productFlavor (ru / en). */
val appLanguage: Language
    get() = runCatching { Language.valueOf(BuildConfig.DEFAULT_LANGUAGE) }
        .getOrDefault(Language.RU)

/** Локализованная подпись темы (data-слой держит свой маппинг приватным). */
fun StoryTheme.localizedLabel(language: Language = appLanguage): String = when (language) {
    Language.RU -> labelRu
    Language.EN -> labelEn
}
