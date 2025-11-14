package com.example.petcaresistemadecontroleerotinaparapets

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.petcaresistemadecontroleerotinaparapets.presentation.navigation.AppNavigation
import com.example.petcaresistemadecontroleerotinaparapets.ui.theme.PetCareSistemaDeControleERotinaParaPetsTheme
import dagger.hilt.android.AndroidEntryPoint

// 1. Anota  ção obrigatória para o Hilt injetar ViewModels
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 2. Sua lógica de permissão (mantida do seu arquivo)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM_PERMISSION", "Permissão de notificações CONCEDIDA!")
        } else {
            Log.d("FCM_PERMISSION", "Permissão de notificações NEGADA.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pede a permissão (mantido)
        askNotificationPermission()

        // 3. O setContent agora chama sua Navegação
        setContent {
            PetCareSistemaDeControleERotinaParaPetsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Esta função é o "mapa" do seu app
                    AppNavigation()
                }
            }
        }
    }

    // Sua função de permissão (mantida)
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}