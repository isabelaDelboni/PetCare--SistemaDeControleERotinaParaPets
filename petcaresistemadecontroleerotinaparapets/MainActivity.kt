package com.example.petcaresistemadecontroleerotinaparapets

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirestoreService
import com.example.petcaresistemadecontroleerotinaparapets.ui.theme.PetCareSistemaDeControleERotinaParaPetsTheme
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {

    // Launcher para pedir permissão de notificação (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("TESTE_FIREBASE", "✅ Permissão de notificações CONCEDIDA!")
        } else {
            Log.e("TESTE_FIREBASE", "❌ Permissão de notificações NEGADA. O app não vai mostrar alertas.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- PEDIR PERMISSÃO (Android 13+) ---
        askNotificationPermission()

        // --- SEU CÓDIGO DE TESTE ANTIGO CONTINUA AQUI ---
        lifecycleScope.launch {
            // ... (Mantenha o código de teste que você já tinha aqui)
            // Se quiser, pode só manter a parte de pegar o token:
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                Log.d("TESTE_FIREBASE", "SEU TOKEN: $token")
            } catch (e: Exception) { }
        }

        setContent {
            PetCareSistemaDeControleERotinaParaPetsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting("Android", Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun askNotificationPermission() {
        // Só precisa pedir se for Android 13 (API 33) ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("TESTE_FIREBASE", "Permissão já concedida anteriormente.")
            } else {
                // Pede a permissão
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PetCareSistemaDeControleERotinaParaPetsTheme {
        Greeting("Android")
    }
}