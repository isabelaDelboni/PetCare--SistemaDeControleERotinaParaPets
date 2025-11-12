package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.petcaresistemadecontroleerotinaparapets.presentation.components.AppButton
import com.example.petcaresistemadecontroleerotinaparapets.presentation.components.AppTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Evento") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Adicione um novo evento para o seu pet, como uma consulta, vacina ou banho.",
                style = MaterialTheme.typography.bodyMedium
            )

            AppTextField(
                value = title,
                onValueChange = { title = it },
                label = "Título do Evento"
            )

            AppTextField(
                value = date,
                onValueChange = { date = it },
                label = "Data (ex: 25/12/2025)"
            )

            AppButton(
                text = "Salvar Evento",
                onClick = {
                    if (title.isNotBlank() && date.isNotBlank()) {
                        // Lógica para salvar o evento (ViewModel)
                        Toast.makeText(context, "Evento '$title' salvo!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}
