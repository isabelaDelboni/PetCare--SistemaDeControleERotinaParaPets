package com.example.petcaresistemadecontroleerotinaparapets.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: FirebaseAuthService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val loginState = mutableStateOf<LoginState>(LoginState.Idle)

    val googleSignInClient: GoogleSignInClient

    init {
        val webClientId = "301129245613-lijf67i6e1hjjqmmikmd7j53urher41c.apps.googleusercontent.com"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signInWithGoogle(idToken: String) {
        loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = authService.signInWithGoogleCredential(idToken)
            result.onSuccess {
                loginState.value = LoginState.Success
            }.onFailure { exception ->
                loginState.value = LoginState.Error(exception.message ?: "Erro no login com Google")
            }
        }
    }

    fun login() {
        if (email.value.isBlank() || password.value.isBlank()) {
            loginState.value = LoginState.Error("E-mail e senha não podem estar em branco.")
            return
        }
        loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = authService.signIn(email.value, password.value)
            result.onSuccess {
                loginState.value = LoginState.Success
            }.onFailure { exception ->
                loginState.value = LoginState.Error(exception.message ?: "Erro desconhecido no login")
            }
        }
    }

    fun signUp() {
        if (email.value.isBlank() || password.value.isBlank()) {
            loginState.value = LoginState.Error("Email e Senha não podem estar vazios.")
            return
        }
        loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = authService.signUp(email.value, password.value)
            result.onSuccess {
                loginState.value = LoginState.Success
            }.onFailure { exception ->
                loginState.value = LoginState.Error(exception.message ?: "Erro ao criar conta.")
            }
        }
    }

    fun getCurrentUser() = authService.getCurrentUser()

    fun signOut() {
        authService.signOut()
        googleSignInClient.signOut()
        loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}