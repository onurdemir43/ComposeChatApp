package com.onurdemir.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onurdemir.chatapp.ui.ChatListScreen
import com.onurdemir.chatapp.ui.LoginScreen
import com.onurdemir.chatapp.ui.ProfileScreen
import com.onurdemir.chatapp.ui.SignupScreen
import com.onurdemir.chatapp.ui.SingleChatScreen
import com.onurdemir.chatapp.ui.SingleStatusScreen
import com.onurdemir.chatapp.ui.StatusListScreen
import com.onurdemir.chatapp.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(val route: String) {
    object Signup: DestinationScreen("signup")
    object Login: DestinationScreen("login")
    object Profile: DestinationScreen("profile")
    object ChatList: DestinationScreen("chatList")
    object SingleChat: DestinationScreen("singleChat/{chatId}") {
        fun creatRoute(id: String) = "singleChat/$id"
    }
    object StatusList: DestinationScreen("statusList")
    object SingleStatus: DestinationScreen("singleStatus/{statusId}") {
        fun createRoute(id: String) = "singleStatus/$id"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }
}

@Composable
fun ChatAppNavigation() {
    val vm = hiltViewModel<CAViewModel>()

    NotificationMessage(vm = vm)

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = DestinationScreen.Signup.route) {
        composable(DestinationScreen.Signup.route) {
            SignupScreen(navController, vm)
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController, vm)
        }
        composable(DestinationScreen.Profile.route) {
            ProfileScreen(navController, vm)
        }
        composable(DestinationScreen.StatusList.route) {
            StatusListScreen(navController)
        }
        composable(DestinationScreen.SingleStatus.route) {
            SingleStatusScreen(statusId = "123")
        }
        composable(DestinationScreen.ChatList.route) {
            ChatListScreen(navController)
        }
        composable(DestinationScreen.SingleChat.route) {
            SingleChatScreen(chatId = "123")
        }

    }

}