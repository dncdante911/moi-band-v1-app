package com.moi.band.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moi.band.player.PlayerViewModel
import com.moi.band.ui.player.FullPlayerScreen
import com.moi.band.ui.player.MiniPlayer
import com.moi.band.ui.screens.*

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Albums : BottomNavItem("albums", Icons.Default.Album, "Альбомы")
    object News : BottomNavItem("news", Icons.Default.Newspaper, "Новости")
    object Gallery : BottomNavItem("gallery", Icons.Default.Photo, "Галерея")
    object Chat : BottomNavItem("chat", Icons.Default.Chat, "Чат")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Профиль")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToAlbumDetail: (Int) -> Unit,
    onNavigateToNewsDetail: (Int) -> Unit,
    onNavigateToChatRoom: (Int) -> Unit,
    onNavigateToLogin: () -> Unit,  // ← ДОБАВЬ ЭТО
    onLogout: () -> Unit,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showFullPlayer by remember { mutableStateOf(false) }
    
    val currentTrack by playerViewModel.currentTrack.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()
    val currentPosition by playerViewModel.currentPosition.collectAsState()
    val duration by playerViewModel.duration.collectAsState()
    
    val items = listOf(
        BottomNavItem.Albums,
        BottomNavItem.News,
        BottomNavItem.Gallery,
        BottomNavItem.Chat,
        BottomNavItem.Profile
    )

    if (showFullPlayer) {
        FullPlayerScreen(
            onDismiss = { showFullPlayer = false },
            viewModel = playerViewModel
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            text = when (selectedTab) {
                                0 -> "🎸 Альбомы"
                                1 -> "📰 Новости"
                                2 -> "📷 Галерея"
                                3 -> "💬 Чат"
                                4 -> "👤 Профиль"
                                else -> "Master of Illusion"
                            }
                        ) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
            }
        ) { paddingValues ->
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> AlbumsScreen(
                        onAlbumClick = onNavigateToAlbumDetail
                    )
                    1 -> NewsScreen(
                        onNewsClick = onNavigateToNewsDetail
                    )
                    2 -> GalleryScreen()
                    3 -> ChatRoomsScreen(
                        onRoomClick = onNavigateToChatRoom
                    )
                    4 -> ProfileScreen(
                        onLogout = onLogout
                    )
                }
                
                // Mini Player at bottom
                if (currentTrack != null) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                    ) {
                        MiniPlayer(
                            track = currentTrack,
                            isPlaying = isPlaying,
                            currentPosition = currentPosition,
                            duration = duration,
                            onPlayPauseClick = { playerViewModel.playPause() },
                            onNextClick = { playerViewModel.next() },
                            onExpandClick = { showFullPlayer = true },
                            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}