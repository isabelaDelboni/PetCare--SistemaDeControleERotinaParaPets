package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.LoginState

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit // <-- PARÂMETRO ADICIONADO
) {
    val email by authViewModel.email
    val password by authViewModel.password
    val state by authViewModel.loginState
    val context = LocalContext.current

    // Observa o estado do login
    LaunchedEffect(state) {
        when (val currentState = state) {
            is LoginState.Success -> {
                Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                onLoginSuccess() // Navega para a próxima tela
            }
            is LoginState.Error -> {
                Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
            }
            else -> {} // Idle ou Loading
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PetCare+", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { authViewModel.email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { authViewModel.password.value = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.login() },
            enabled = state != LoginState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state == LoginState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Entrar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // <-- Espaçador adicionado

        // --- BOTÃO DE CRIAR CONTA ADICIONADO ---
        TextButton(onClick = onSignUpClick) {
            Text("Não tem uma conta? Criar cadastro")
        }
        // --- FIM DA ADIÇÃO ---
    }
}