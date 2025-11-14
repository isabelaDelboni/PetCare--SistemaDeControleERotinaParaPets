package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.R // <-- Importe o R
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

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
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current


    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                // Sucesso! Pega o ID Token e manda para o ViewModel
                authViewModel.signInWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "Erro no login com Google: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Image(
                painter = painterResource(id = R.drawable.logo_petcare),
                contentDescription = "Logo PetCare",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
            )

            Text(
                "Bem-vindo ao PetCare+",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Organize a rotina do seu pet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(40.dp))

            // Campo de E-mail
            OutlinedTextField(
                value = email,
                onValueChange = { authViewModel.email.value = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Senha
            OutlinedTextField(
                value = password,
                onValueChange = { authViewModel.password.value = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Senha")
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Ver senha")
                    }
                }
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botão de Login
            Button(
                onClick = { authViewModel.login() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = loginState != LoginState.Loading
            ) {
                Text("Login")
            }

            // Separador "OU"
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text("OU", modifier = Modifier.padding(horizontal = 8.dp), style = MaterialTheme.typography.bodySmall)
                Divider(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = {
                    googleSignInLauncher.launch(authViewModel.googleSignInClient.signInIntent)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("G", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 8.dp))
                Text("Entrar com Google", color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Cadastro
            TextButton(onClick = onSignUpClick) {
                Text("Não tem uma conta? Cadastre-se")
            }

            // Indicador de Carregamento
            if (loginState == LoginState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}