package com.example.storytime

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.data.util.Constants
import com.example.data.worker.StoryReminderWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class StoryTimeApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        // Прокидываем секреты из BuildConfig в data-слой (он не зависит от app).
        Constants.YANDEX_API_KEY = BuildConfig.YANDEX_API_KEY
        Constants.YANDEX_FOLDER_ID = BuildConfig.YANDEX_FOLDER_ID

        // Планируем ежедневное напоминание о сказке (REPLACE → идемпотентно).
        StoryReminderWorker.scheduleNextReminder(this)
    }
}
