package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
// IMPORT CORRETO (para o ícone "Voltar")
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    petViewModel: PetViewModel,
    authViewModel: AuthViewModel,
    onPetSaved: () -> Unit // Função de callback para navegar de volta
) {
    // Estados para controlar os campos de texto
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }

    val context = LocalContext.current
    val currentUser = authViewModel.getCurrentUser()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Novo Pet") },
                navigationIcon = {
                    // Botão para voltar (chama o onPetSaved, que no Nav é popBackStack)
                    IconButton(onClick = onPetSaved) {
                        // CORREÇÃO: Ícone AutoMirrored
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
                .verticalScroll(rememberScrollState()), // Permite rolar se a tela for pequena
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Preencha as informações do seu pet:",
                style = MaterialTheme.typography.titleMedium
            )

            // Campo Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            // Campo Espécie
            OutlinedTextField(
                value = especie,
                onValueChange = { especie = it },
                label = { Text("Espécie * (ex: Cachorro, Gato)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            // Campo Raça
            OutlinedTextField(
                value = raca,
                onValueChange = { raca = it },
                label = { Text("Raça (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            // Campo Idade
            OutlinedTextField(
                value = idade,
                onValueChange = { idade = it },
                label = { Text("Idade (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Salvar
            Button(
                onClick = {
                    // Validação dos dados
                    if (nome.isBlank() || especie.isBlank()) {
                        Toast.makeText(context, "Nome e Espécie são obrigatórios.", Toast.LENGTH_SHORT).show()
                    } else if (currentUser == null) {
                        Toast.makeText(context, "Erro: Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 1. Cria o objeto Pet
                        val novoPet = Pet(
                            nome = nome,
                            especie = especie,
                            raca = raca.takeIf { it.isNotBlank() }, // Salva null se estiver em branco
                            idade = idade.toIntOrNull(), // Converte para Int ou null
                            usuarioId = currentUser.uid // Pega o ID do Firebase
                        )

                        // 2. Chama o ViewModel para salvar
                        petViewModel.adicionarPet(novoPet)

                        // 3. Mostra feedback e navega de volta
                        Toast.makeText(context, "$nome salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        onPetSaved() // Chama a navegação de volta
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Pet")
            }
        }
    }
}