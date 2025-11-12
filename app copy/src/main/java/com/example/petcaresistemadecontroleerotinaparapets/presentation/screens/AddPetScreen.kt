package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    petViewModel: PetViewModel,
    authViewModel: AuthViewModel, // (Pode ser usado para ID do usuário, mas o ViewModel já o obtém)
    onPetSaved: () -> Unit
) {
    // Estados para os campos de texto
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }

    val context = LocalContext.current
    val uiState by petViewModel.uiState.collectAsState()

    // Observa o estado da UI para feedback
    LaunchedEffect(uiState) {
        if (uiState is PetUiState.Error) {
            Toast.makeText(context, (uiState as PetUiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Novo Pet") },
                navigationIcon = {
                    IconButton(onClick = onPetSaved) { // 'onPetSaved' aqui age como "Voltar"
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Permite rolar se os campos não couberem
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Insira os dados do seu pet", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = especie,
                onValueChange = { especie = it },
                label = { Text("Espécie * (ex: Cachorro, Gato)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = raca,
                onValueChange = { raca = it },
                label = { Text("Raça (ex: SRD, Poodle)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = idade,
                onValueChange = { idade = it },
                label = { Text("Idade") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Chama o ViewModel para adicionar o pet
                    petViewModel.addPet(nome, especie, raca, idade)

                    // Se a validação do ViewModel passar (não for Error), volta
                    if (nome.isNotBlank() && especie.isNotBlank()) {
                        Toast.makeText(context, "$nome salvo!", Toast.LENGTH_SHORT).show()
                        onPetSaved() // Navega de volta
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Pet")
            }
        }
    }
}