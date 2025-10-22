# 🎵 Audio Player - Полное руководство

## ✅ Что реализовано

### 1. **MusicPlayerService** - Background Service
- Работает в фоне даже при свернутом приложении
- MediaSession для управления из уведомлений
- Интеграция с системным медиаплеером
- Автоматическая обработка headphone disconnect

### 2. **MusicPlayerManager** - Менеджер плеера
- Управление ExoPlayer
- Плейлисты и очереди треков
- Автоматическое переключение треков
- Repeat modes: Off, All, One
- Shuffle mode
- Seek to position
- Отслеживание прогресса в реальном времени

### 3. **PlayerViewModel** - MVVM ViewModel
- Централизованное управление состоянием
- Flow для реактивности
- Автоматическая запись прослушиваний через API
- Интеграция с TrackRepository

### 4. **MiniPlayer** - Компактный плеер
- Отображается внизу экрана поверх контента
- Показывает текущий трек, обложку, прогресс
- Play/Pause и Next кнопки
- Клик для раскрытия в Full Player
- Автоматически показывается при запуске трека

### 5. **FullPlayerScreen** - Полноэкранный плеер
- Большая обложка альбома
- Slider для перемотки
- Полный контроль: Previous, Play/Pause, Next
- Shuffle и Repeat кнопки
- Время: текущее / общее
- Красивый Material 3 дизайн

### 6. **AlbumDetailScreen** - С интеграцией плеера
- Кнопка "Играть все" для всего альбома
- Каждый трек кликабельный
- Индикатор проигрываемого трека
- Анимированная иконка для playing треков

### 7. **Offline Mode** - Room Database
- Кэширование альбомов, треков, новостей, галереи
- Автоматическое сохранение при загрузке
- Чтение из кэша при отсутствии интернета
- Автоматическая очистка устаревших данных

### 8. **DownloadManager** - Скачивание треков
- Скачивание MP3 файлов на устройство
- Отслеживание прогресса скачивания
- Хранение в локальной папке
- Воспроизведение из локального файла
- Управление скачанными файлами
- Подсчёт занятого места

## 📁 Структура файлов плеера

```
app/src/main/java/com/moi/band/
├── player/
│   ├── MusicPlayerService.kt        # ✅ Background Service
│   ├── MusicPlayerManager.kt        # ✅ Player Manager
│   ├── PlayerViewModel.kt           # ✅ ViewModel
│   └── PlayerEventListener.kt       # ✅ Event Listener
├── ui/player/
│   ├── MiniPlayer.kt                # ✅ Compact player bar
│   └── FullPlayerScreen.kt          # ✅ Full screen player
├── data/local/
│   └── AppDatabase.kt               # ✅ Room Database
└── download/
    └── DownloadManager.kt           # ✅ Download Manager
```

## 🎮 Как использовать плеер

### Воспроизведение одного трека

```kotlin
// В любом Composable с доступом к PlayerViewModel
val playerViewModel: PlayerViewModel = hiltViewModel()

// Запустить трек
playerViewModel.playTrack(track)
```

### Воспроизведение плейлиста

```kotlin
// Запустить весь альбом с 3-го трека
playerViewModel.playPlaylist(
    tracks = album.tracks,
    startIndex = 2
)
```

### Управление воспроизведением

```kotlin
// Play/Pause
playerViewModel.playPause()

// Следующий трек
playerViewModel.next()

// Предыдущий трек
playerViewModel.previous()

// Перемотка на позицию (в миллисекундах)
playerViewModel.seekTo(30000) // 30 секунд

// Repeat mode
playerViewModel.toggleRepeat()

// Shuffle
playerViewModel.toggleShuffle()
```

### Отслеживание состояния

```kotlin
val currentTrack by playerViewModel.currentTrack.collectAsState()
val isPlaying by playerViewModel.isPlaying.collectAsState()
val currentPosition by playerViewModel.currentPosition.collectAsState()
val duration by playerViewModel.duration.collectAsState()

// Использование
Text("Сейчас играет: ${currentTrack?.title}")
Text("${formatTime(currentPosition)} / ${formatTime(duration)}")
```

## 🎵 MiniPlayer интеграция

MiniPlayer автоматически показывается в MainScreen когда трек начинает играть:

```kotlin
// В MainScreen
if (currentTrack != null) {
    MiniPlayer(
        track = currentTrack,
        isPlaying = isPlaying,
        currentPosition = currentPosition,
        duration = duration,
        onPlayPauseClick = { playerViewModel.playPause() },
        onNextClick = { playerViewModel.next() },
        onExpandClick = { showFullPlayer = true }
    )
}
```

## 💾 Offline Mode - Room Database

### Автоматическое кэширование

```kotlin
// В Repository - автоматически сохраняет в БД
suspend fun getAlbums(): Flow<Resource<List<Album>>> = flow {
    emit(Resource.Loading())
    
    try {
        // Сначала загружаем из кэша
        val cached = albumDao.getAllAlbums().map { it.toAlbum() }
        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached))
        }
        
        // Затем с API
        val response = api.getAlbums()
        if (response.isSuccessful) {
            val albums = response.body()?.data ?: emptyList()
            
            // Сохраняем в кэш
            albumDao.insertAlbums(albums.map { it.toEntity() })
            
            emit(Resource.Success(albums))
        }
    } catch (e: Exception) {
        // При ошибке возвращаем кэш
        val cached = albumDao.getAllAlbums().map { it.toAlbum() }
        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached))
        } else {
            emit(Resource.Error(e.message ?: "Error"))
        }
    }
}
```

### Очистка устаревшего кэша

```kotlin
// Очистить кэш старше 7 дней
val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
albumDao.deleteExpiredAlbums(sevenDaysAgo)
```

## 📥 Download Manager

### Скачать трек

```kotlin
val downloadManager: DownloadManager = /* inject */

// Скачать трек
viewModelScope.launch {
    val result = downloadManager.downloadTrack(track)
    result.onSuccess { localPath ->
        println("Скачано: $localPath")
    }.onFailure { error ->
        println("Ошибка: ${error.message}")
    }
}
```

### Отслеживание прогресса

```kotlin
val downloadProgress by downloadManager.downloadProgress.collectAsState()

// Для конкретного трека
val progress = downloadProgress[track.id]
when (progress?.status) {
    DownloadStatus.DOWNLOADING -> {
        LinearProgressIndicator(progress = progress.progress)
        Text("${(progress.progress * 100).toInt()}%")
    }
    DownloadStatus.COMPLETED -> {
        Icon(Icons.Default.CheckCircle, "Скачано")
    }
    DownloadStatus.FAILED -> {
        Icon(Icons.Default.Error, "Ошибка")
    }
    else -> {}
}
```

### Проверить скачан ли трек

```kotlin
val isDownloaded = downloadManager.isTrackDownloaded(track.id)

if (isDownloaded) {
    // Воспроизвести из локального файла
    val localPath = downloadManager.getDownloadedTrackPath(track.id)
    // Используй localPath вместо URL
}
```

### Удалить скачанный трек

```kotlin
downloadManager.deleteDownload(track.id)
```

### Статистика скачиваний

```kotlin
val allDownloads = downloadManager.getAllDownloads()
val totalSize = downloadManager.getDownloadSize()
val formattedSize = downloadManager.formatSize(totalSize)

Text("Скачано треков: ${allDownloads.size}")
Text("Занято места: $formattedSize")
```

## 🎛️ Управление из уведомления

Плеер автоматически создаёт MediaSession notification с кнопками:
- ⏮️ Previous
- ⏯️ Play/Pause
- ⏭️ Next

Работает даже при свернутом приложении!

## 🔊 Режимы воспроизведения

### Repeat Modes

```kotlin
// Off - воспроизведение один раз
Player.REPEAT_MODE_OFF

// All - повтор всего плейлиста
Player.REPEAT_MODE_ALL

// One - повтор одного трека
Player.REPEAT_MODE_ONE

// Переключение
playerViewModel.toggleRepeat()
```

### Shuffle Mode

```kotlin
// Включить/выключить shuffle
playerViewModel.toggleShuffle()

// Проверить состояние
val shuffleEnabled by playerViewModel.shuffleEnabled.collectAsState()
```

## 📊 Отслеживание прослушиваний

Каждое воспроизведение автоматически записывается через API:

```kotlin
// В PlayerViewModel
private fun recordPlay(trackId: Int) {
    viewModelScope.launch {
        trackRepository.playTrack(trackId).collect { /* Handle result */ }
    }
}

// Вызывается автоматически при:
// - playTrack()
// - playPlaylist()
// - next()
// - previous()
```

## 🎨 UI Components

### MiniPlayer Компоненты

```kotlin
MiniPlayer(
    track = currentTrack,           // Текущий трек
    isPlaying = isPlaying,          // Играет ли
    currentPosition = currentPos,   // Текущая позиция (ms)
    duration = duration,            // Длительность (ms)
    onPlayPauseClick = { },        // Обработчик Play/Pause
    onNextClick = { },             // Обработчик Next
    onExpandClick = { }            // Раскрыть в Full Player
)
```

### TrackItem в AlbumDetailScreen

```kotlin
TrackItem(
    track = track,
    trackNumber = index + 1,
    isCurrentTrack = currentTrack?.id == track.id,
    isPlaying = isPlaying && currentTrack?.id == track.id,
    onClick = { playerViewModel.playPlaylist(tracks, index) }
)
```

## 🔧 Настройки и оптимизация

### ExoPlayer настройки в MusicPlayerService

```kotlin
val audioAttributes = AudioAttributes.Builder()
    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
    .setUsage(C.USAGE_MEDIA)
    .build()

player = ExoPlayer.Builder(this)
    .setAudioAttributes(audioAttributes, true)
    .setHandleAudioBecomingNoisy(true)  // Pause при отключении наушников
    .build()
```

### Оптимизация для батареи

- WakeLock только во время воспроизведения
- Foreground Service для фонового воспроизведения
- Автоматическая остановка при длительном бездействии

### Кэширование ExoPlayer

ExoPlayer автоматически кэширует аудио:
- Предзагрузка следующего трека
- Буферизация для плавного воспроизведения
- Кэш в памяти для быстрого доступа

## 📱 Permissions

Необходимые разрешения уже добавлены в AndroidManifest:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## 🐛 Troubleshooting

### Плеер не запускается

1. Проверь URL трека: `BuildConfig.BASE_URL + track.audioUrl`
2. Убедись что MusicPlayerService зарегистрирован в Manifest
3. Проверь Logcat на ошибки ExoPlayer

### Нет звука

1. Проверь громкость медиа (не рингтон!)
2. Убедись что AudioAttributes настроены правильно
3. Проверь формат аудио файла (MP3 поддерживается)

### Прерывается при сворачивании

1. MusicPlayerService должен быть Foreground Service
2. Проверь что notification отображается
3. Проверь battery optimization settings

### Медленная загрузка

1. Проверь интернет соединение
2. Оптимизируй размер аудио файлов на сервере
3. Используй CDN для статики

## 📈 Статистика и аналитика

Отслеживай:
- Количество прослушиваний через API
- Популярные треки
- Время прослушивания
- Skip rate (пропуски треков)
- Скачанные треки

```kotlin
// В ProfileScreen можно добавить
Text("Всего прослушиваний: ${user.statistics?.plays}")
```

## 🚀 Будущие улучшения

### Можно добавить:

1. **Эквалайзер** - Настройка звука
2. **Lyrics** - Синхронизированные тексты песен
3. **Sleep Timer** - Автоостановка через N минут
4. **Crossfade** - Плавные переходы между треками
5. **Gapless Playback** - Без пауз между треками
6. **Speed Control** - Изменение скорости воспроизведения
7. **Visualizer** - Визуализация звука
8. **Car Mode** - Упрощённый интерфейс для авто
9. **Smart Playlists** - Автоматические подборки
10. **Share** - Поделиться треком

## 💡 Примеры использования

### Создать плейлист "Избранное"

```kotlin
@Entity(tableName = "favorites")
data class FavoriteTrack(
    @PrimaryKey val trackId: Int,
    val addedAt: Long = System.currentTimeMillis()
)

// Добавить в избранное
favoriteDao.insertFavorite(FavoriteTrack(trackId = track.id))

// Получить все избранные
val favorites = favoriteDao.getAllFavorites()

// Воспроизвести избранное
playerViewModel.playPlaylist(favoritesTracks, 0)
```

### История прослушиваний

```kotlin
@Entity(tableName = "play_history")
data class PlayHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trackId: Int,
    val playedAt: Long = System.currentTimeMillis()
)

// Записать в историю
historyDao.insertHistory(PlayHistoryItem(trackId = track.id))

// Последние 50 прослушанных
val recent = historyDao.getRecentHistory(limit = 50)
```

### Очередь воспроизведения

```kotlin
// Добавить трек в очередь
val currentPlaylist = playerViewModel.playlist.value.toMutableList()
currentPlaylist.add(track)
playerViewModel.playPlaylist(currentPlaylist, currentPlaylist.size - 1)
```

## ✅ Checklist готовности

- [x] **MusicPlayerService** - Background service
- [x] **MusicPlayerManager** - Player management
- [x] **PlayerViewModel** - MVVM integration
- [x] **MiniPlayer** - Compact player UI
- [x] **FullPlayerScreen** - Full screen player
- [x] **AlbumDetailScreen** - Integration with albums
- [x] **Offline Mode** - Room Database caching
- [x] **DownloadManager** - Download tracks
- [x] **MediaSession** - System integration
- [x] **Notification Controls** - Background controls
- [x] **Repeat/Shuffle** - Playback modes
- [x] **Progress Tracking** - Real-time updates
- [x] **API Integration** - Record plays

## 🎉 Результат

**Полностью функциональный музыкальный плеер** с:
- ✅ Фоновым воспроизведением
- ✅ Управлением из уведомлений
- ✅ Плейлистами и очередями
- ✅ Offline режимом
- ✅ Скачиванием треков
- ✅ Красивым Material 3 UI
- ✅ Полной интеграцией с API

---

**Готово к использованию! 🎸**

Теперь у тебя есть профессиональный музыкальный плеер, сопоставимый с Spotify, Apple Music и другими топовыми приложениями!