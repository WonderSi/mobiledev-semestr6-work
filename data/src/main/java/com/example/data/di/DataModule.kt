package com.example.data.di

import com.example.data.firebase.FirestoreDataSource
import com.example.data.firebase.RemoteConfigDataSource
import com.example.data.remote.YandexGptApi
import com.example.data.remote.YandexGptDataSource
import com.example.data.repository.StoryRepositoryImpl
import com.example.data.util.Constants
import com.example.domain.repository.StoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.YANDEX_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideYandexGptApi(retrofit: Retrofit): YandexGptApi =
        retrofit.create(YandexGptApi::class.java)

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().also {
        val settings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }
        it.setConfigSettingsAsync(settings)
        it.setDefaultsAsync(
            mapOf(
                "yandex_prompt_ru" to "Напиши сказку на ночь для ребёнка по имени {name} на тему \"{theme}\". Сказка должна быть доброй, с моралью, длиной 5-7 абзацев. Начни с названия сказки на первой строке.",
                "yandex_prompt_en" to "Write a bedtime story for a child named {name} about \"{theme}\". The story should be kind, with a moral, 5-7 paragraphs long. Start with the story title on the first line."
            )
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStoryRepository(impl: StoryRepositoryImpl): StoryRepository
}
