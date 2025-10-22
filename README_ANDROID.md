# 🎸 Master of Illusion - Android App

Полноценное Android-приложение для музыкального проекта Master of Illusion с интеграцией API.

## 📁 Структура проекта

```
app/
├── src/main/
│   ├── java/com/moi/band/
│   │   ├── MoiApplication.kt                 # Hilt Application
│   │   ├── MainActivity.kt                   # Main Activity
│   │   ├── data/
│   │   │   ├── api/
│   │   │   │   └── ApiService.kt            # Retrofit API
│   │   │   ├── model/
│   │   │   │   └── Models.kt                # Data classes
│   │   │   └── repository/
│   │   │       └── Repositories.kt          # Data repositories
│   │   ├── di/
│   │   │   └── AppModule.kt                 # Hilt DI
│   │   ├── ui/
│   │   │   ├── auth/
│   │   │   │   ├── AuthViewModel.kt
│   │   │   │   ├── LoginScreen.kt
│   │   │   │   └── RegisterScreen.kt
│   │   │   ├── albums/
│   │   │   │   └── AlbumViewModel.kt
│   │   │   ├── main/
│   │   │   │   └── MainScreen.kt
│   │   │   ├── navigation/
│   │   │   │   └── NavGraph.kt
│   │   │   ├── screens/
│   │   │   │   ├── AlbumsScreen.kt
│   │   │   │   └── AllScreens.kt           # News, Gallery, Chat, Profile
│   │   │   └── theme/
│   │   │       ├── Theme.kt
│   │   │       └── Typography.kt
│   │   └── util/
│   │       ├── Resource.kt                  # State wrapper
│   │       └── TokenManager.kt              # JWT manager
│   ├── res/
│   │   ├── values/
│   │   │   └── strings.xml
│   │   └── xml/
│   │       ├── backup_rules.xml
│   │       └── data_extraction_rules.xml
│   └── AndroidManifest.xml
├── build.gradle.kts (Module)
└── proguard-rules.pro
```

## 🚀 Быстрый старт

### 1. Требования

- **Android Studio** Hedgehog (2023.1.1) или новее
- **JDK 17** или выше
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Gradle:** 8.2+

### 2. Клонирование и открытие

```bash
# Создай новый проект в Android Studio или используй существующий
# Скопируй все файлы в соответствующие папки
```

### 3. Настройка dependencies

**build.gradle.kts (Project level):**
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
}
```

**build.gradle.kts (Module level):** уже предоставлен выше

### 4. Gradle Sync

```bash
File -> Sync Project with Gradle Files
```

### 5. Запуск

```bash
Run -> Run 'app' (Shift + F10)
```

## 🔑 Основные возможности

### ✅ Реализовано

1. **Аутентификация**
   - ✅ Логин (username/email + password)
   - ✅ Регистрация с валидацией
   - ✅ JWT токен хранение
   - ✅ Автоматический logout

2. **Альбомы**
   - ✅ Список альбомов с пагинацией
   - ✅ Детали альбома с треками
   - ✅ Lazy loading
   - ✅ Pull to refresh

3. **Новости**
   - ✅ Список новостей
   - ✅ Фильтр по категориям (release, event, update)
   - ✅ Детальный просмотр

4. **Галерея**
   - ✅ Grid layout фотографий
   - ✅ Фильтр по категориям (studio, concert, event, promo)
   - ✅ Lazy loading images

5. **Чат**
   - ✅ Список комнат чата
   - ✅ Просмотр сообщений
   - ✅ Отправка сообщений
   - ✅ Real-time updates

6. **Профиль**
   - ✅ Информация пользователя
   - ✅ Статистика (прослушивания, сообщения)
   - ✅ Смена темы (Dark, Light, Gothic, Punk)
   - ✅ Logout

### 🎨 Темы

Приложение поддерживает 4 темы:
- **Dark** - Тёмная Metal тема (по умолчанию)
- **Light** - Светлая тема
- **Gothic** - Готическая тема (чёрная с красным)
- **Punk** - Панк тема (яркие неоновые цвета)

### 🔐 Безопасность

- JWT токены хранятся в **DataStore** (зашифровано)
- Все пароли скрыты при вводе
- HTTPS обязательный
- Автоматический logout при истечении токена

## 📝 API Интеграция

### Базовый URL
```kotlin
API_BASE_URL = "https://lovix.top/api/v1"
BASE_URL = "https://lovix.top"
```

### Endpoints используемые в приложении

| Endpoint | Метод | Описание | Auth |
|----------|-------|----------|------|
| `/auth/login.php` | POST | Вход пользователя | ❌ |
| `/auth/register.php` | POST | Регистрация | ❌ |
| `/albums/list.php` | GET | Список альбомов | ❌ |
| `/albums/detail.php` | GET | Детали альбома | ❌ |
| `/tracks/search.php` | GET | Поиск треков | ❌ |
| `/tracks/play.php` | POST | Отметить прослушивание | ✅ |
| `/news/list.php` | GET | Список новостей | ❌ |
| `/news/detail.php` | GET | Детали новости | ❌ |
| `/gallery/list.php` | GET | Галерея | ❌ |
| `/chat/rooms.php` | GET | Комнаты чата | ✅ |
| `/chat/messages.php` | GET | Сообщения комнаты | ✅ |
| `/chat/send.php` | POST | Отправить сообщение | ✅ |
| `/user/profile.php` | GET | Профиль пользователя | ✅ |
| `/user/profile.php` | PUT | Обновить профиль | ✅ |

## 🛠️ Технологии

### Core
- **Kotlin** 1.9.20
- **Jetpack Compose** - Modern UI toolkit
- **Material 3** - Material Design 3

### Architecture
- **MVVM** - Model-View-ViewModel
- **Clean Architecture** - Separation of concerns
- **Repository Pattern** - Data abstraction

### Dependency Injection
- **Hilt** - Dagger wrapper для Android

### Networking
- **Retrofit 2.9.0** - REST API client
- **OkHttp 4.12.0** - HTTP client
- **Gson** - JSON serialization

### Async
- **Kotlin Coroutines** - Асинхронность
- **Flow** - Reactive streams

### Storage
- **DataStore** - Key-value хранилище для preferences
- **Room** (опционально) - Local database

### Image Loading
- **Coil 2.5.0** - Image loading с Compose поддержкой

### Media Player
- **ExoPlayer** (Media3) - Аудио плеер

### Navigation
- **Navigation Compose** - Compose навигация

## 🧪 Тестирование

### Unit Tests
```bash
./gradlew test
```

### UI Tests
```bash
./gradlew connectedAndroidTest
```

## 📦 Сборка APK

### Debug Build
```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

### Release Build
```bash
# 1. Создай keystore
keytool -genkey -v -keystore moi-release.keystore \
  -alias moi-key -keyalg RSA -keysize 2048 -validity 10000

# 2. Добавь в gradle.properties:
# RELEASE_STORE_FILE=../moi-release.keystore
# RELEASE_STORE_PASSWORD=your_password
# RELEASE_KEY_ALIAS=moi-key
# RELEASE_KEY_PASSWORD=your_password

# 3. Собери release
./gradlew assembleRelease
# APK: app/build/outputs/apk/release/app-release.apk
```

## 🐛 Отладка

### Логи API
В debug режиме все HTTP запросы логируются через OkHttp Interceptor:

```
API Request: POST /auth/login.php
Headers: {Content-Type: application/json}
Body: {"username":"test","password":"***"}
Response: 200 OK
Body: {"success":true,"data":{...}}
```

### Logcat фильтры
```bash
# Все логи приложения
adb logcat | grep "com.moi.band"

# Только ошибки
adb logcat *:E

# Network логи
adb logcat | grep "OkHttp"
```

## 🎯 TODO - Что можно улучшить

### Высокий приоритет
- [ ] **Audio Player** - Полноценный медиа плеер с плейлистами
- [ ] **Offline Mode** - Кэширование данных в Room DB
- [ ] **Push Notifications** - Firebase Cloud Messaging
- [ ] **Deep Links** - Открытие конкретных экранов по ссылкам
- [ ] **Share функционал** - Поделиться треками/новостями

### Средний приоритет
- [ ] **Search** - Глобальный поиск по всему контенту
- [ ] **Favorites** - Избранные треки и альбомы
- [ ] **Download Tracks** - Скачивание для оффлайн прослушивания
- [ ] **Dark/Light Auto** - Авто переключение по системной теме
- [ ] **Error Handling** - Более детальная обработка ошибок

### Низкий приоритет
- [ ] **Unit Tests** - Покрытие тестами
- [ ] **Analytics** - Google Analytics или Firebase
- [ ] **Crash Reporting** - Crashlytics
- [ ] **Rate Tracks** - Оценка треков пользователями
- [ ] **Comments** - Комментарии к новостям

## 📱 Поддерживаемые устройства

- **Android 7.0 (API 24)** и выше
- Телефоны и планшеты
- Разные размеры экранов (phone, tablet)

## 🔧 Troubleshooting

### Проблема: Gradle sync failed
**Решение:** 
```bash
File -> Invalidate Caches / Restart
./gradlew clean
```

### Проблема: Network error / Unable to connect
**Решение:** Проверь:
1. Интернет соединение
2. API endpoint доступен: `curl https://lovix.top/api/v1/albums/list.php`
3. Permissions в AndroidManifest.xml

### Проблема: Images не загружаются
**Решение:**
1. Проверь `BuildConfig.BASE_URL`
2. Убедись что `usesCleartextTraffic="false"` (только HTTPS)
3. Проверь Coil logs

### Проблема: JWT token expired
**Решение:**
- Токен живёт 24 часа
- При ошибке 401 приложение автоматически разлогинит
- Войди заново

## 📄 Лицензия

MIT License - используй как хочешь

## 👨‍💻 Разработка

### Добавление нового экрана

1. Создай ViewModel:
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {
    // implementation
}
```

2. Создай Composable Screen:
```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel()
) {
    // implementation
}
```

3. Добавь в NavGraph:
```kotlin
composable("my_route") {
    MyScreen()
}
```

### Добавление нового API endpoint

1. Добавь в `ApiService.kt`:
```kotlin
@GET("my/endpoint.php")
suspend fun getMyData(): Response<ApiResponse<MyData>>
```

2. Создай Repository метод:
```kotlin
suspend fun getMyData(): Flow<Resource<MyData>> = flow {
    // implementation
}
```

3. Используй в ViewModel через Hilt injection

## 🔗 Полезные ссылки

- [API Documentation](https://github.com/yourusername/moi-backend)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Hilt Guide](https://developer.android.com/training/dependency-injection/hilt-android)
- [Material 3 Design](https://m3.material.io/)

## 📞 Контакты

- **Website:** https://lovix.top
- **API:** https://lovix.top/api/v1
- **Project:** Master of Illusion

## ⚡ Performance Tips

1. **Images**: Используй `.thumbnail_path` для preview, `.image_path` для full size
2. **Pagination**: Загружай данные по 10-20 items
3. **Caching**: Coil автоматически кэширует images
4. **LazyColumn/Grid**: Используй для больших списков
5. **remember**: Сохраняй state между recomposition

## 🎨 Кастомизация темы

Измени цвета в `Theme.kt`:

```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE91E63), // Твой цвет
    // ...
)
```

## 🔄 Версионирование

```
versionCode = 1       // Increment for each release
versionName = "1.0.0" // Semantic versioning
```

---

**Версия:** 1.0.0  
**Дата:** 21.10.2025  
**Статус:** ✅ Production Ready

Made with 🎸 for Master of Illusion