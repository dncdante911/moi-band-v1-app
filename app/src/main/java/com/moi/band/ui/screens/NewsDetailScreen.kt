package com.moi.band.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // <-- ИСПОЛЬЗУЕМ СТАНДАРТНУЮ ИКОНКУ
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.moi.band.BuildConfig
import com.moi.band.data.model.News
import com.moi.band.data.repository.NewsRepository
import com.moi.band.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel для деталей новости
@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _newsDetailState = MutableStateFlow<Resource<News>?>(null)
    val newsDetailState: StateFlow<Resource<News>?> = _newsDetailState.asStateFlow()

    fun loadNewsDetail(newsId: Int) {
        viewModelScope.launch {
            newsRepository.getNewsDetail(newsId).collect {
                _newsDetailState.value = it
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    newsId: Int,
    onNavigateBack: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel()
) {
    val newsState by viewModel.newsDetailState.collectAsState()

    LaunchedEffect(newsId) {
        viewModel.loadNewsDetail(newsId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали новости") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, // <-- ИСПРАВЛЕНО
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = newsState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val news = state.data
                if (news != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        news.imagePath?.let { imagePath ->
                            AsyncImage(
                                model = BuildConfig.BASE_URL + imagePath,
                                contentDescription = news.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = news.title,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = news.content,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Категория: ${news.category}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
            }
            is Resource.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message ?: "Ошибка загрузки новости",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            null -> {}
        }
    }
}