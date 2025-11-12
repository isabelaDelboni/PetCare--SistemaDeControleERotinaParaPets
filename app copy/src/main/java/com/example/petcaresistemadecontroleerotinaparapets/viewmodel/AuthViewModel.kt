package com.example.petcaresistemadecontroleerotinaparapets.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    // 1. Injeta o Serviço de Autenticação
    private val authService: FirebaseAuthService
) : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    // 2. Controla o estado da UI (Carregando, Erro, Sucesso)
    val loginState = mutableStateOf<LoginState>(LoginState.Idle)

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

    // --- FUNÇÃO SIGNUP CORRIGIDA ---
    fun signUp() {
        // Validação simples
        if (email.value.isBlank() || password.value.isBlank()) {
            loginState.value = LoginState.Error("Email e Senha não podem estar vazios.")
            return
        }

        loginState.value = LoginState.Loading

        // ✅ CORREÇÃO: Usando viewModelScope para chamar a suspend fun 'signUp'
        viewModelScope.launch {
            // ✅ CORREÇÃO: Chamando a função 'signUp' do serviço, que retorna um Result
            val result = authService.signUp(email.value, password.value) //

            result.onSuccess {
                // Sucesso! O LaunchedEffect na tela de SignUp irá capturar isso.
                loginState.value = LoginState.Success
            }.onFailure { exception ->
                // Erro
                // ✅ CORREÇÃO: 'exception.message' agora é válido
                loginState.value = LoginState.Error(exception.message ?: "Erro ao criar conta.")
            }
        }
    }
    // --- FIM DA CORREÇÃO ---

    fun getCurrentUser() = authService.getCurrentUser()

    fun signOut() {
        authService.signOut()
        loginState.value = LoginState.Idle
    }
}

// 3. Classe para representar os estados
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}