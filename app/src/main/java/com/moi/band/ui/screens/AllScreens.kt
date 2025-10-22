@file:OptIn(ExperimentalMaterial3Api::class)

package com.moi.band.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.moi.band.BuildConfig
import com.moi.band.data.model.*
import com.moi.band.data.repository.*
import com.moi.band.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ========== NEWS VIEWMODEL ==========
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _newsState = MutableStateFlow<Resource<PaginatedResponse<News>>?>(null)
    val newsState: StateFlow<Resource<PaginatedResponse<News>>?> = _newsState

    init {
        loadNews()
    }

    fun loadNews(category: String? = null) {
        viewModelScope.launch {
            newsRepository.getNews(1, category).collect {
                _newsState.value = it
            }
        }
    }
}

// ========== NEWS SCREEN ==========
@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    onNewsClick: (Int) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val newsState by viewModel.newsState.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        // Category Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = {
                    selectedCategory = null
                    viewModel.loadNews(null)
                },
                label = { Text("Все") }
            )
            FilterChip(
                selected = selectedCategory == "release",
                onClick = {
                    selectedCategory = "release"
                    viewModel.loadNews("release")
                },
                label = { Text("Релизы") }
            )
            FilterChip(
                selected = selectedCategory == "event",
                onClick = {
                    selectedCategory = "event"
                    viewModel.loadNews("event")
                },
                label = { Text("События") }
            )
        }

        when (val state = newsState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val newsList = state.data?.data ?: emptyList()
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(newsList) { news ->
                        NewsCard(news = news, onClick = { onNewsClick(news.id) })
                    }
                }
            }
            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message ?: "Ошибка", color = MaterialTheme.colorScheme.error)
                }
            }
            null -> {}
        }
    }
}

@Composable
fun NewsCard(news: News, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            news.imagePath?.let { imagePath ->
                AsyncImage(
                    model = BuildConfig.BASE_URL + imagePath,
                    contentDescription = news.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                news.excerpt?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = news.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = news.createdAt,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ========== GALLERY VIEWMODEL ==========
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {
    private val _galleryState = MutableStateFlow<Resource<PaginatedResponse<GalleryImage>>?>(null)
    val galleryState: StateFlow<Resource<PaginatedResponse<GalleryImage>>?> = _galleryState

    init {
        loadGallery()
    }

    fun loadGallery(category: String? = null) {
        viewModelScope.launch {
            galleryRepository.getGallery(1, category).collect {
                _galleryState.value = it
            }
        }
    }
}

// ========== GALLERY SCREEN ==========
@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val galleryState by viewModel.galleryState.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        // Category Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = {
                    selectedCategory = null
                    viewModel.loadGallery(null)
                },
                label = { Text("Все") }
            )
            FilterChip(
                selected = selectedCategory == "studio",
                onClick = {
                    selectedCategory = "studio"
                    viewModel.loadGallery("studio")
                },
                label = { Text("Студия") }
            )
            FilterChip(
                selected = selectedCategory == "concert",
                onClick = {
                    selectedCategory = "concert"
                    viewModel.loadGallery("concert")
                },
                label = { Text("Концерт") }
            )
        }

        when (val state = galleryState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val images = state.data?.data ?: emptyList()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(images) { image ->
                        GalleryImageCard(image)
                    }
                }
            }
            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message ?: "Ошибка", color = MaterialTheme.colorScheme.error)
                }
            }
            null -> {}
        }
    }
}

@Composable
fun GalleryImageCard(image: GalleryImage) {
    Card(
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = BuildConfig.BASE_URL + (image.thumbnailPath ?: image.imagePath),
            contentDescription = image.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}

// ========== CHAT VIEWMODEL ==========
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _roomsState = MutableStateFlow<Resource<List<ChatRoom>>?>(null)
    val roomsState: StateFlow<Resource<List<ChatRoom>>?> = _roomsState

    init {
        loadRooms()
    }

    fun loadRooms() {
        viewModelScope.launch {
            chatRepository.getChatRooms().collect {
                _roomsState.value = it
            }
        }
    }
}

// ========== CHAT ROOMS SCREEN ==========
@Composable
fun ChatRoomsScreen(
    modifier: Modifier = Modifier,
    onRoomClick: (Int) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val roomsState by viewModel.roomsState.collectAsState()

    when (val state = roomsState) {
        is Resource.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Success -> {
            val rooms = state.data ?: emptyList()
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rooms) { room ->
                    ChatRoomCard(room = room, onClick = { onRoomClick(room.id) })
                }
            }
        }
        is Resource.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        state.message ?: "Ошибка",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadRooms() }) {
                        Text("Повторить")
                    }
                }
            }
        }
        null -> {}
    }
}

@Composable
fun ChatRoomCard(room: ChatRoom, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = room.icon ?: "💬",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium
                )
                
                room.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                room.lastMessage?.let { lastMsg ->
                    Text(
                        text = "${lastMsg.username}: ${lastMsg.text}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            room.messagesCount?.let {
                Badge {
                    Text(it.toString())
                }
            }
        }
    }
}

// ========== PROFILE VIEWMODEL ==========
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow<Resource<User>?>(null)
    val profileState: StateFlow<Resource<User>?> = _profileState

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            userRepository.getProfile().collect {
                _profileState.value = it
            }
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            userRepository.updateProfile(null, null, theme, null).collect {
                if (it is Resource.Success) {
                    loadProfile()
                }
            }
        }
    }
}

// ========== PROFILE SCREEN ==========
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    when (val state = profileState) {
        is Resource.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Success -> {
            val user = state.data
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = user?.displayName ?: "",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            
                            Text(
                                text = "@${user?.username}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            user?.bio?.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Статистика",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            user?.statistics?.let { stats ->
                                StatRow("Прослушиваний", stats.plays.toString())
                                StatRow("Сообщений", stats.roomMessages.toString())
                                StatRow("Избранное", stats.favorites.toString())
                            }
                        }
                    }
                }

                item {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Тема приложения",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = user?.theme == "dark",
                                    onClick = { viewModel.updateTheme("dark") },
                                    label = { Text("Dark") }
                                )
                                FilterChip(
                                    selected = user?.theme == "light",
                                    onClick = { viewModel.updateTheme("light") },
                                    label = { Text("Light") }
                                )
                                FilterChip(
                                    selected = user?.theme == "gothic",
                                    onClick = { viewModel.updateTheme("gothic") },
                                    label = { Text("Gothic") }
                                )
                                FilterChip(
                                    selected = user?.theme == "punk",
                                    onClick = { viewModel.updateTheme("punk") },
                                    label = { Text("Punk") }
                                )
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Logout, "Выход")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Выйти")
                    }
                }
            }
        }
        is Resource.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.message ?: "Ошибка", color = MaterialTheme.colorScheme.error)
            }
        }
        null -> {}
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}