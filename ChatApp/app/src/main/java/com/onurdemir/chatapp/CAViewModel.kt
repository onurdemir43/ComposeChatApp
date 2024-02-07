package com.onurdemir.chatapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.onurdemir.chatapp.data.COLLECTION_USER
import com.onurdemir.chatapp.data.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CAViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage): ViewModel() {

    val inProgress = mutableStateOf(false)
    val popUpNotification = mutableStateOf<Event<String>?>(null)
    val signedIn = mutableStateOf(false)

    @SuppressLint("SuspiciousIndentation")
    fun onSignup(name: String, number: String, email: String, password: String) {
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill in all fields")
            return
        }
        inProgress.value = true
        db.collection(COLLECTION_USER).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty && password == "123456")
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        signedIn.value = true
                        //create user profile
                    } else {
                        handleException(task.exception, "Signup failed")
                    }
                }
                else
                    handleException(customMessage = "number already exists or invalid password")
                    inProgress.value = false
        }
            .addOnFailureListener {
                handleException(it)
            }
    }

    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.d("ChatApp", "Chat app exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        popUpNotification.value = Event(message)
        inProgress.value = false
    }
}