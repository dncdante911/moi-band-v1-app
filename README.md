# 🎸 Master of Illusion - Android Application

**Полное мобильное приложение для Master of Illusion** музыкального проекта.

- **Platform:** Android 8.0+ (API 24+)
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM + Clean Architecture

---

## 📋 Структура проекта

```
app/
├── src/main/
│   ├── java/com/moibandmusic/app/
│   │   ├── data/
│   │   │   ├── local/              # Room Database
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── AlbumDao.kt
│   │   │   │   ├── TrackDao.kt
│   │   │   │   └── ... другие Dao
│   │   │   ├── model/              # Data classes
│   │   │   │   └── Models.kt       # Все модели
│   │   │   ├── remote/             # Retrofit API
│   │   │   │   ├── ApiService.kt
│   │   │   │   └── RetrofitClient.kt
│   │   │   └── repository/         # Repositories
│   │   │       ├── AuthRepository.kt
│   │   │       ├── AlbumRepository.kt
│   │   │       ├── TrackRepository.kt
│   │   │       ├── NewsRepository.kt
│   │   │       ├── GalleryRepository.kt
│   │   │       ├── ChatRepository.kt
│   │   │       └── UserRepository.kt
│   │   ├── domain/
│   │   │   └── usecase/            # Use cases
│   │   │       ├── LoginUseCase.kt
│   │   │       ├── GetAlbumsUseCase.kt
│   │   │       └── ...
│   │   ├── presentation/
│   │   │   ├── viewmodel/          # ViewModels
│   │   │   │   ├── AuthViewModel.kt
│   │   │   │   ├── AlbumViewModel.kt
│   │   │   │   ├── PlayerViewModel.kt
│   │   │   │   ├── NewsViewModel.kt
│   │   │   │   ├── GalleryViewModel.kt
│   │   │   │   ├── ChatViewModel.kt
│   │   │   │   └── ProfileViewModel.kt
│   │   │   ├── screen/             # Compose Screens
│   │   │   │   ├── auth/
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   └── RegisterScreen.kt
│   │   │   │   ├── album/
│   │   │   │   │   ├── AlbumListScreen.kt
│   │   │   │   │   └── AlbumDetailScreen.kt
│   │   │   │   ├── player/
│   │   │   │   │   └── PlayerScreen.kt
│   │   │   │   ├── news/
│   │   │   │   │   ├── NewsListScreen.kt
│   │   │   │   │   └── NewsDetailScreen.kt
│   │   │   │   ├── gallery/
│   │   │   │   │   └── GalleryScreen.kt
│   │   │   │   ├── chat/
│   │   │   │   │   ├── ChatRoomsScreen.kt
│   │   │   │   │   └── ChatMessagesScreen.kt
│   │   │   │   └── profile/
│   │   │   │       └── ProfileScreen.kt
│   │   │   ├── component/          # Reusable components
│   │   │   │   ├── AlbumCard.kt
│   │   │   │   ├── TrackCard.kt
│   │   │   │   ├── NewsCard.kt
│   │   │   │   ├── MusicPlayer.kt
│   │   │   │   └── ...
│   │   │   ├── navigation/
│   │   │   │   └── NavGraph.kt
│   │   │   └── theme/
│   │   │       ├── Theme.kt
│   │   │       ├── Color.kt
│   │   │       └── Typography.kt
│   │   ├── util/
│   │   │   ├── TokenManager.kt     # JWT токен хранилище
│   │   │   ├── Constants.kt
│   │   │   └── Extensions.kt
│   │   └── di/                     # Dependency Injection (Hilt)
│   │       ├── AppModule.kt
│   │       ├── RepositoryModule.kt
│   │       └── DatabaseModule.kt
│   ├── res/
│   │   ├── drawable/               # Изображения
│   │   ├── values/
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   └── dimens.xml
│   │   └── ...
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

---

## 🚀 Требования для запуска

- **Android Studio** версия 2023.1.1 или выше
- **Java 17+** (JDK)
- **SDK Level 34** установлен
- **Gradle 8.1** или выше

---

## 📥 Установка и запуск

### 1. Клонировать репозиторий

```bash
git clone https://github.com/yourusername/moi-band-android.git
cd moi-band-android
```

### 2. Открыть в Android Studio

```bash
Android Studio -> Open Project -> выбрать папку
```

### 3. Дождаться синхронизации Gradle

```bash
Build -> Make Project
```

### 4. Запустить приложение

```bash
Run -> Run 'app' (Shift + F10)
```

---

## 🔐 Конфигурация API

Все API endpoints настроены в `build.gradle.kts`:

**Debug:**
```kotlin
buildConfigField("String", "API_URL", "\"https://lovix.top/api/v1\"")
```

Для локального тестирования измени на:
```kotlin
buildConfigField("String", "API_URL", "\"http://192.168.1.X:80/api/v1\"")
```

---

## 💾 Локальное хранилище

### Room Database
Используется для кэширования:
- Альбомы и треки
- Новости
- Галерея
- История чатов

Миграции выполняются автоматически.

### Datastore для предпочтений
Хранит:
- JWT токен
- Тема (dark/light/gothic/punk)
- ID пользователя
- Настройки уведомлений

---

## 📱 Основные экраны

### 1. Auth Screens
- **LoginScreen** - Вход по username/email и пароль
- **RegisterScreen** - Регистрация с валидацией

### 2. Album Screen
- **AlbumListScreen** - Список всех альбомов с пагинацией
- **AlbumDetailScreen** - Детали альбома, список треков

### 3. Player Screen
- Встроенный аудиоплеер с ExoPlayer
- Регулировка громкости
- Перемотка трека
- История прослушиваний синхронизируется с API

### 4. News Screen
- **NewsListScreen** - Список новостей с фильтром по категориям
- **NewsDetailScreen** - Полный текст новости с навигацией

### 5. Gallery Screen
- Галерея фотографий в сетке
- Фильтр по категориям (studio, concert, event, promo)
- Просмотр в полный экран

### 6. Chat Screen
- **ChatRoomsScreen** - Список комнат с количеством сообщений
- **ChatMessagesScreen** - Чат с real-time сообщениями
- Отправка и получение сообщений

### 7. Profile Screen
- Профиль пользователя
- Редактирование имени и биографии
- Выбор темы (dark/light/gothic/punk)
- Статистика (прослушивания, сообщения, избранные)

---

## 🎨 Тема и цвета

### Master of Illusion Palette
```
Primary:     #FFD700 (Gold)
Secondary:   #FFA500 (Orange)
Dark BG:     #0A0A0F
Card BG:     #1A1410
Text Light:  #CCCCCC
Text Dark:   #666666
```

---

## 📡 Сетевые запросы

### Авторизация
```kotlin
// Login
POST /auth/login.php
Body: { username, password }
Response: { token, expires_in, user }

// Register
POST /auth/register.php
Body: { username, email, password, display_name }
Response: { token, expires_in, user }
```

### Используется Retrofit + OkHttp
- Автоматическое добавление JWT токена в заголовки
- Кэширование сетевых ответов (4 часа)
- Логирование запросов (только debug)

---

## 🎵 Аудиоплеер

Используется **ExoPlayer** (MediaPlayer 3):
- Поддержка MP3, WAV
- Управление громкостью
- Синхронизация позиции
- Фоновое воспроизведение

```kotlin
// Пример инициализации
val exoPlayer = ExoPlayer.Builder(context).build()
val mediaItem = MediaItem.fromUri(audioUrl)
exoPlayer.setMediaItem(mediaItem)
exoPlayer.prepare()
exoPlayer.play()
```

---

## 🔍 Поиск

Поиск треков с параметрами:
```kotlin
// GET /api/v1/tracks/search.php?q=название&page=1&per_page=20
searchTracks(query = "Драконов", page = 1)
```

---

## 🛠️ Dependency Injection (Hilt)

### Модули

**AppModule.kt** - Retrofit, OkHttp
```kotlin
@Provides
@Singleton
fun provideApiService(): ApiService
```

**RepositoryModule.kt** - Repositories
```kotlin
@Provides
@Singleton
fun provideAlbumRepository(api: ApiService, db: AppDatabase)
```

**DatabaseModule.kt** - Room Database
```kotlin
@Provides
@Singleton
fun provideAppDatabase(): AppDatabase
```

---

## 📊 ViewModels

Каждый ViewModel управляет своим состоянием:

```kotlin
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow<AlbumState>(AlbumState.Loading)
    val state = _state.asStateFlow()
    
    fun loadAlbums(page: Int) {
        viewModelScope.launch {
            try {
                val albums = repository.getAlbums(page)
                _state.value = AlbumState.Success(albums)
            } catch (e: Exception) {
                _state.value = AlbumState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class AlbumState {
    object Loading : AlbumState()
    data class Success(val albums: List<Album>) : AlbumState()
    data class Error(val message: String) : AlbumState()
}
```

---

## 🧪 Тестирование

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

---

## 📦 Build и Release

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### Подписание APK
```bash
keytool -genkey -v -keystore moi-band-key.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias moi-band-key
```

---

## 🐛 Логирование

Все запросы и ошибки логируются:

### Debug режим
```
API Request: GET /albums/list.php?page=1
Response Code: 200
Response Time: 234ms
```

### Production режим
- Логирование отключено
- Ошибки отправляются на сервер

---

## 📝 Файлы конфигурации

### local.properties
```properties
sdk.dir=/path/to/android/sdk
```

### build.gradle.kts
```kotlin
android {
    compileSdk = 34
    minSdk = 24
    targetSdk = 34
}
```

---

## 🔗 Полезные ссылки

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Hilt Dependency Injection](https://dagger.dev/hilt/)
- [Room Database](https://developer.android.com/jetpack/androidx/releases/room)
- [ExoPlayer](https://github.com/google/ExoPlayer)
- [Retrofit](https://square.github.io/retrofit/)

---

## 📄 Лицензия

MIT License - используй как хочешь

---

## 🤝 Контрибьютинг

Для добавления функционала:

1. Создай fork репозитория
2. Сделай changes: `git checkout -b feature/AmazingFeature`
3. Commit: `git commit -m 'Add some AmazingFeature'`
4. Push: `git push origin feature/AmazingFeature`
5. Open Pull Request

---

## 👤 Автор

**Master of Illusion** - Музыкальный проект  
API: https://lovix.top/api/v1

---

## ❓ FAQ

**Q: Как скачать музыку для оффлайна?**
A: В текущей версии потоковое воспроизведение. Для оффлайна добавь кнопку загрузки в PlayerScreen.

**Q: Как работает аутентификация?**
A: JWT токен хранится в Datastore и автоматически добавляется в заголовки всех защищённых запросов.

**Q: Как обновить тему?**
A: Выбери тему в ProfileScreen, она сохранится в Datastore и применится ко всему приложению.

**Q: Как добавить push notifications?**
A: Используй Firebase Cloud Messaging (FCM). Добавь GoogleServices.json и инициализируй FCM в MainActivity.

---

**Версия:** 1.0  
**Последнее обновление:** 21.10.2025
