package com.moi.band.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moi.band.ui.auth.LoginScreen
import com.moi.band.ui.auth.RegisterScreen
import com.moi.band.ui.main.MainScreen
import com.moi.band.ui.screens.AlbumDetailScreen
import com.moi.band.ui.screens.NewsDetailScreen
import com.moi.band.ui.screens.ChatRoomScreen
import com.moi.band.util.TokenManager
import kotlinx.coroutines.runBlocking

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Login : Screen("login")
    object Register : Screen("register")
    object AlbumDetail : Screen("album/{albumId}") {
        fun createRoute(albumId: Int) = "album/$albumId"
    }
    object NewsDetail : Screen("news/{newsId}") {
        fun createRoute(newsId: Int) = "news/$newsId"
    }
    object ChatRoom : Screen("chat/{roomId}") {
        fun createRoute(roomId: Int) = "chat/$roomId"
    }
}

@Composable
fun NavGraph(
    tokenManager: TokenManager,
    navController: NavHostController = rememberNavController()
) {
    // ВСЕГДА начинаем с Main, не проверяем логин
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        // Main Screen - доступен БЕЗ логина
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToAlbumDetail = { albumId ->
                    navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                },
                onNavigateToNewsDetail = { newsId ->
                    navController.navigate(Screen.NewsDetail.createRoute(newsId))
                },
                onNavigateToChatRoom = { roomId ->
                    navController.navigate(Screen.ChatRoom.createRoute(roomId))
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onLogout = {
                    // Just stay on main screen
                }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.popBackStack(Screen.Main.route, inclusive = false)
                }
            )
        }

        // Album Detail
        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.IntType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0
            AlbumDetailScreen(
                albumId = albumId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // News Detail
        composable(
            route = Screen.NewsDetail.route,
            arguments = listOf(navArgument("newsId") { type = NavType.IntType })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getInt("newsId") ?: 0
            NewsDetailScreen(
                newsId = newsId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Chat Room
        composable(
            route = Screen.ChatRoom.route,
            arguments = listOf(navArgument("roomId") { type = NavType.IntType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getInt("roomId") ?: 0
            ChatRoomScreen(
                roomId = roomId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}