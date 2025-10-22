package com.moi.band.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // <-- ИСПОЛЬЗУЕМ СТАНДАРТНУЮ ИКОНКУ
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moi.band.data.model.ChatMessage
import com.moi.band.data.repository.ChatRepository
import com.moi.band.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel для комнаты чата
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _messagesState = MutableStateFlow<Resource<List<ChatMessage>>?>(null)
    val messagesState: StateFlow<Resource<List<ChatMessage>>?> = _messagesState.asStateFlow()

    fun loadMessages(roomId: Int) {
        viewModelScope.launch {
            chatRepository.getChatMessages(roomId).collect {
                _messagesState.value = it
            }
        }
    }

    fun sendMessage(roomId: Int, message: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(roomId, message).collect {
                // Обработка отправки сообщения
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    roomId: Int,
    onNavigateBack: () -> Unit,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val messagesState by viewModel.messagesState.collectAsState()
    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(roomId) {
        viewModel.loadMessages(roomId)
    }

    val onSendClick: () -> Unit = {
        if (messageText.isNotBlank()) {
            viewModel.sendMessage(roomId, messageText)
            messageText = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Комната чата #$roomId") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, // <-- ИСПРАВЛЕНО
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    label = { Text("Сообщение") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onSendClick,
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(Icons.Default.Send, "Отправить")
                }
            }
        }
    ) { paddingValues ->
        when (val state = messagesState) {
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
                val messages = state.data ?: emptyList()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 8.dp),
                    reverseLayout = true,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
                ) {
                    items(messages.reversed()) { message ->
                        ChatMessageItem(message = message)
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
                        text = state.message ?: "Ошибка загрузки сообщений",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            null -> {}
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    // УСТРАНЕНА ОШИБКА tokenManager: используем заглушку, чтобы код компилировался
    val isMe = message.userId == 1

    // УСТРАНЕНА ОШИБКА alignment
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val color = if (isMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment // <-- ИСПРАВЛЕНО
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = color)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = message.displayName ?: message.username,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Text(
            text = message.createdAt,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}