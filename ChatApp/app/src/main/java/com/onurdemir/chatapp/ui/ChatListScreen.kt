package com.onurdemir.chatapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.onurdemir.chatapp.CAViewModel
import com.onurdemir.chatapp.CommonProgressSpinner
import com.onurdemir.chatapp.CommonRow
import com.onurdemir.chatapp.DestinationScreen
import com.onurdemir.chatapp.TitleText
import com.onurdemir.chatapp.navigateTo

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavController, vm: CAViewModel) {
    val inProgress = vm.inProgressChats.value
    if (inProgress) CommonProgressSpinner()
    else {
        val chats = vm.chats.value
        val userData = vm.userData.value

        val showDialog = remember { mutableStateOf(false) }
        val onFabClick: () -> Unit = {showDialog.value = true}
        val onDissmis: () -> Unit = {showDialog.value = false}
        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it)
            showDialog.value = false
        }

        Scaffold(floatingActionButton = {
            FAB(showDialog = showDialog.value,
            onFabClick = onFabClick,
            onDismiss = onDissmis,
            onAddChat = onAddChat)
        },
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                TitleText(txt = "Chats")
                if (chats.isEmpty())
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No chats available")
                    }
                else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chats) { chat ->
                            val chatUser = if (chat.user1.userId == userData?.userId) chat.user2
                                else chat.user1
                            CommonRow(
                                imageUrl = chatUser.imageUrl ?: "",
                                name = chatUser.name
                            ) {
                                chat.chatId?.let { id ->
                                    navigateTo(navController, DestinationScreen.SingleChat.createRoute(id = id))
                                }
                            }

                        }
                    }
                }
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.CHATLIST,
                    navController = navController
                )
            }
        })


    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember { mutableStateOf("") }
    if (showDialog)
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
            addChatNumber.value = ""
    },
    confirmButton = {
        Button(onClick = {
            onAddChat(addChatNumber.value)
            addChatNumber.value = ""
        }) {
            Text(text = "Add chat")
        }
    },
    title = { Text(text = "Add chat")},
    text = {
        OutlinedTextField(value = addChatNumber.value,
            onValueChange = { addChatNumber.value = it },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )}
    )
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
    }


}