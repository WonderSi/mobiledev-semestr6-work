# Сказки на ночь

Приложение для генерации детских сказок на основе YandexGPT.
Родитель вводит имя ребёнка и тему — нейросеть создаёт уникальную
сказку. Каждый день в 21:00 приходит push-уведомление.

## Скриншоты

<div align="center">

| | | | |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/7efafb2f-70f8-4c18-af76-5fb9150a591b" width="200"/> | <img src="https://github.com/user-attachments/assets/8bc1a821-998e-4d86-9389-892d5b1f03c1" width="200"/> | <img src="https://github.com/user-attachments/assets/b0dab68a-35ca-4b17-a5b7-edec9e5fa519" width="200"/> | <img src="https://github.com/user-attachments/assets/5c427a37-655d-4601-9db8-24391f1ec582" width="200"/> |
| <img src="https://github.com/user-attachments/assets/0aa955e4-9026-42b0-b899-dd39c60dc4bd" width="200"/> | <img src="https://github.com/user-attachments/assets/03a968c2-9704-4175-8243-ff6777f7a53e" width="200"/> | <img src="https://github.com/user-attachments/assets/9a698f1a-567a-464e-b5a2-1d08a62af8c3" width="200"/> | <img src="https://github.com/user-attachments/assets/42ca1c5a-050a-4ce2-bd04-b2a1a8730572" width="200"/> |

</div>

https://github.com/user-attachments/assets/8bd4282f-223a-4fe0-a1f5-83a3d7b817fe

https://github.com/user-attachments/assets/650a00ac-d737-4ae4-83ba-bb97b138a6ce

https://github.com/user-attachments/assets/9d011561-27cb-40ca-b32f-2fbc9eba4c24

https://github.com/user-attachments/assets/73b7662a-4d94-411f-8e1a-7cf6ab5f6508

https://github.com/user-attachments/assets/f3762311-3401-4054-8364-2e2bba3a1f2f


## Технологии
- Jetpack Compose, Hilt, WorkManager
- YandexGPT API, Firebase (Firestore, FCM, Remote Config, Crashlytics)
- Clean Architecture: 3 Gradle модуля (app / domain / data)

## Критерии

### Чистая архитектура (5б)
- Модули: `domain/`, `data/`, `app/`
- Use Cases: `domain/src/.../usecase/`
- DI: `data/src/.../di/DataModule.kt`, `app/src/.../di/AppModule.kt`
- Маппинг DTO→Domain: `data/src/.../repository/StoryRepositoryImpl.kt`

### Фоновые задачи (3б)
- WorkManager: `data/src/.../worker/StoryReminderWorker.kt`
- BroadcastReceiver: `data/src/.../receiver/BootReceiver.kt`

### Анимации Compose (2б)
- AnimatedVisibility (абзацы): `app/src/.../ui/story/StoryScreen.kt`
- animateFloatAsState (кнопка): `app/src/.../ui/home/HomeScreen.kt`

### XML + Compose (2б)
- `app/src/main/res/layout/activity_splash.xml`
- `app/src/.../ui/splash/SplashActivity.kt`

### Gradle Flavors (2б)
- `app/build.gradle.kts` — productFlavors ru/en

### Firebase (+2б)
- Firestore: `data/src/.../firebase/FirestoreDataSource.kt`
- FCM: `data/src/.../worker/StoryReminderWorker.kt`
- Remote Config: `data/src/.../firebase/RemoteConfigDataSource.kt`

### YandexGPT AI (+2б)
- `data/src/.../remote/YandexGptDataSource.kt`
- `data/src/.../remote/YandexGptApi.kt`

### Crashlytics (+1б)
- Подключён в `app/build.gradle.kts`
- Инициализируется автоматически через Firebase

## Сборка
- `ruDebug` — разработка (русская версия)
- `ruRelease` — финальный APK
