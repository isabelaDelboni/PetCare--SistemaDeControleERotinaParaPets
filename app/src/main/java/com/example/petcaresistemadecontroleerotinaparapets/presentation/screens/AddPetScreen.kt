package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    viewModel: PetViewModel,
    onBackClick: () -> Unit,
    onPetSaved: () -> Unit
) {
    // Estados dos campos de texto
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }

    // Estado da Foto (URI temporária no celular)
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // Configurador da Galeria
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // Quando o usuário escolhe a foto, salvamos a URI aqui
        if (uri != null) {
            fotoUri = uri
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Pet") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- ÁREA DA FOTO (Clicável) ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {
                        // Abre a galeria filtrando apenas imagens
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (fotoUri != null) {
                    // Se tem foto escolhida, mostra ela usando COIL
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto do Pet",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Se não tem, mostra ícone de câmera
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Adicionar Foto",
                        modifier = Modifier.size(40.dp),
                        tint = Color.DarkGray
                    )
                }
            }
            Text("Toque para adicionar foto", style = MaterialTheme.typography.bodySmall)

            // --- CAMPOS DE TEXTO ---
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Pet") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = especie,
                onValueChange = { especie = it },
                label = { Text("Espécie (Cachorro, Gato...)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = raca,
                onValueChange = { raca = it },
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = idade,
                onValueChange = { idade = it },
                label = { Text("Idade (anos)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTÃO SALVAR ---
            Button(
                onClick = {
                    if (nome.isBlank() || especie.isBlank()) {
                        Toast.makeText(context, "Preencha nome e espécie!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Chama o ViewModel passando a foto
                        viewModel.adicionarPet(
                            nome = nome,
                            especie = especie,
                            raca = raca,
                            idade = idade.toIntOrNull() ?: 0,
                            fotoUri = fotoUri // <--- Passamos a URI aqui
                        )
                        Toast.makeText(context, "Salvando pet...", Toast.LENGTH_SHORT).show()
                        onPetSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Pet")
            }
        }
    }
}