# 🚀 Quick Start Guide - Master of Illusion Android App

## ⚡ За 5 минут до первого запуска

### Шаг 1: Установка Android Studio (если ещё нет)

1. Скачай [Android Studio](https://developer.android.com/studio)
2. Установи Android Studio
3. При первом запуске выбери "Standard" installation
4. Дождись установки SDK и эмулятора

### Шаг 2: Создание проекта

```bash
# Вариант А: Создай новый проект
Android Studio -> New Project -> Empty Activity
- Name: MoiBand
- Package: com.moi.band
- Language: Kotlin
- Minimum SDK: API 24

# Вариант Б: Используй существующий проект
```

### Шаг 3: Копирование файлов

Скопируй все файлы в структуру проекта:

```
MoiBand/
├── app/
│   ├── build.gradle.kts          → Замени содержимое
│   ├── proguard-rules.pro        → Замени содержимое
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml    → Замени
│           ├── java/com/moi/band/
│           │   ├── MoiApplication.kt
│           │   ├── MainActivity.kt
│           │   ├── data/...
│           │   ├── di/...
│           │   ├── ui/...
│           │   └── util/...
│           └── res/
│               ├── values/strings.xml
│               └── xml/
│                   ├── backup_rules.xml
│                   └── data_extraction_rules.xml
├── build.gradle.kts               → Замени (project level)
└── gradle.properties              → Замени
```

### Шаг 4: Gradle Sync

```
File → Sync Project with Gradle Files
```

Подожди 2-5 минут пока загрузятся все dependencies.

### Шаг 5: Запуск

```
Run → Run 'app' (или нажми Shift + F10)
```

Выбери эмулятор или подключи реальное устройство.

## 📱 Первый запуск приложения

### Регистрация тестового пользователя

1. При запуске откроется Login Screen
2. Нажми "Зарегистрироваться"
3. Заполни форму:
   - **Username:** testuser
   - **Email:** test@example.com
   - **Display Name:** Test User
   - **Password:** Test123456
   - **Confirm Password:** Test123456
4. Нажми "Зарегистрироваться"
5. После успешной регистрации откроется главный экран

### Вход существующего пользователя

Используй credentials из твоей базы данных:
- **Username:** dncdante (или email)
- **Password:** твой пароль

## 🎯 Основные экраны

### 1. Альбомы (Albums)
- Показывает список всех альбомов
- Клик на альбом → детали с треками
- Scroll вниз → подгрузка следующих альбомов

### 2. Новости (News)
- Список всех новостей
- Фильтры: Все / Релизы / События
- Клик на новость → полный текст

### 3. Галерея (Gallery)
- Сетка фотографий 2 колонки
- Фильтры: Все / Студия / Концерт
- Клик на фото → полноэкранный просмотр (TODO)

### 4. Чат (Chat)
- Список всех комнат чата
- Клик на комнату → сообщения
- Отправка сообщений

### 5. Профиль (Profile)
- Информация о пользователе
- Статистика прослушиваний
- Смена темы приложения
- Кнопка "Выйти"

## 🎨 Смена темы

1. Перейди в **Профиль** (последняя вкладка)
2. Найди секцию "Тема приложения"
3. Выбери одну из тем:
   - **Dark** - Тёмная металлическая тема
   - **Light** - Светлая тема
   - **Gothic** - Готическая (чёрная с красным)
   - **Punk** - Панк (яркие неоновые цвета)
4. Тема применится мгновенно

## 🔧 Настройка для локального API

Если хочешь использовать локальный сервер вместо production API:

1. Открой `app/build.gradle.kts`
2. Найди `buildConfigField`:
```kotlin
buildConfigField("String", "API_BASE_URL", "\"http://192.168.1.X:80/api/v1\"")
buildConfigField("String", "BASE_URL", "\"http://192.168.1.X\"")
```
3. Замени `192.168.1.X` на IP твоего локального сервера
4. Убедись что в `AndroidManifest.xml`:
```xml
android:usesCleartextTraffic="true"  <!-- Только для локального развития -->
```
5. Пересобери проект: `Build → Clean Project` → `Build → Rebuild Project`

## 📊 Проверка работы API

### Тест 1: Загрузка альбомов
```bash
curl https://lovix.top/api/v1/albums/list.php
```
Должен вернуть JSON с альбомами.

### Тест 2: Логин
```bash
curl -X POST https://lovix.top/api/v1/auth/login.php \
  -H "Content-Type: application/json" \
  -d '{"username":"dncdante","password":"твой_пароль"}'
```
Должен вернуть JWT токен.

### Тест 3: Профиль
```bash
curl -X GET https://lovix.top/api/v1/user/profile.php \
  -H "Authorization: Bearer <JWT_TOKEN>"
```
Должен вернуть данные профиля.

## 🐛 Частые проблемы

### Проблема: "Unable to resolve dependency"
**Решение:**
```bash
File → Invalidate Caches / Restart
```

### Проблема: "Could not find com.android.tools.build:gradle:8.2.0"
**Решение:**
```bash
# В build.gradle.kts (Project):
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}
```

### Проблема: "SSL handshake failed"
**Решение:**
Убедись что используешь эмулятор с Google Play или реальное устройство с актуальной версией Android.

### Проблема: "Network Security Configuration"
**Решение:**
Для production всегда используй HTTPS. Для локального dev добавь network security config.

### Проблема: Приложение крашится при запуске
**Решение:**
1. Проверь Logcat: `View → Tool Windows → Logcat`
2. Найди строку с `FATAL EXCEPTION`
3. Проверь правильность всех import statements
4. Убедись что все файлы в правильных пакетах

## 📖 Следующие шаги

1. **Изучи код:**
   - Начни с `MainActivity.kt`
   - Посмотри `NavGraph.kt` для понимания навигации
   - Изучи `AuthViewModel.kt` для понимания MVVM

2. **Добавь функционал:**
   - Реализуй Audio Player
   - Добавь поиск
   - Реализуй избранное
   - Добавь push notifications

3. **Улучши UI:**
   - Добавь анимации
   - Улучши loading states
   - Добавь skeleton loaders
   - Реализуй swipe gestures

4. **Оптимизируй:**
   - Добавь Room DB для кэширования
   - Реализуй offline mode
   - Добавь image compression
   - Оптимизируй списки

## 🎓 Полезные команды

```bash
# Очистка проекта
./gradlew clean

# Сборка debug APK
./gradlew assembleDebug

# Установка на устройство
./gradlew installDebug

# Запуск тестов
./gradlew test

# Проверка lint
./gradlew lint

# Создание release APK
./gradlew assembleRelease
```

## 💡 Pro Tips

1. **Hot Reload:** Используй Ctrl+S для быстрого применения изменений в Compose
2. **Logcat фильтры:** Создай фильтр по имени пакета для чистых логов
3. **Layout Inspector:** `Tools → Layout Inspector` для debug UI
4. **Database Inspector:** `View → Tool Windows → App Inspection` для просмотра DataStore
5. **Network Profiler:** `View → Tool Windows → Profiler` для мониторинга API calls

## ✅ Checklist перед релизом

- [ ] Изменён `versionCode` и `versionName`
- [ ] Создан release keystore
- [ ] Настроен ProGuard
- [ ] Удалены все `TODO` комментарии
- [ ] Проверены все permissions
- [ ] Протестировано на разных устройствах
- [ ] Проверена работа в offline режиме
- [ ] Добавлена политика конфиденциальности
- [ ] Созданы screenshots для Google Play

## 📞 Поддержка

Если что-то не работает:
1. Проверь README.md
2. Посмотри Logcat errors
3. Проверь что API доступен
4. Убедись что все dependencies установлены

---

**Удачи с разработкой! 🎸**

Made with ❤️ for Master of Illusion