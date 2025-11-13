package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.LoginState

// ✅ IMPORT ADICIONADO PARA O BOTÃO DO GOOGLE
import com.example.petcaresistemadecontroleerotinaparapets.presentation.components.GoogleSignInButton
// ✅✅✅ IMPORT CORRIGIDO (ESTAVA FALTANDO) ✅✅✅
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val email by authViewModel.email
    val password by authViewModel.password
    val loginState by authViewModel.loginState

    val context = LocalContext.current

    // Observa o estado do login
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
                authViewModel.loginState.value = LoginState.Idle // Reseta o estado
            }
            is LoginState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.loginState.value = LoginState.Idle // Reseta o estado
            }
            else -> { /* Não faz nada em Idle ou Loading */ }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bem-vindo!", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            // Campo de E-mail
            OutlinedTextField(
                value = email,
                onValueChange = { authViewModel.email.value = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Senha
            OutlinedTextField(
                value = password,
                onValueChange = { authViewModel.password.value = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botão de Login
            Button(
                onClick = { authViewModel.login() },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState != LoginState.Loading // Desativa se estiver carregando
            ) {
                Text("Login")
            }

            // --- SEPARADOR "OU" ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    "OU",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray // ✅ Esta é a linha que estava dando erro
                )
                Divider(modifier = Modifier.weight(1f))
            }

            // --- BOTÃO DO GOOGLE ADICIONADO ---
            GoogleSignInButton(
                onClick = {
                    // A equipe de integração colocará a lógica aqui
                    Toast.makeText(context, "Botão Google Clicado!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            )
            // --- FIM DA ADIÇÃO ---


            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Cadastro
            TextButton(onClick = onSignUpClick) {
                Text("Não tem uma conta? Cadastre-se")
            }

            // Indicador de Carregamento
            if (loginState == LoginState.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}