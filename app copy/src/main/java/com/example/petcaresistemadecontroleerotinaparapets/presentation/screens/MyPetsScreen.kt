package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPetsScreen(
    petViewModel: PetViewModel,
    authViewModel: AuthViewModel,
    onPetClick: (String) -> Unit,
    onAddPetClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // Coleta o StateFlow do ViewModel. A UI será redesenhada automaticamente
    // quando a lista de pets mudar no banco de dados.
    val pets by petViewModel.pets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pets") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurações")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPetClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Pet")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (pets.isEmpty()) {
                // Estado de lista vazia
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Nenhum pet cadastrado.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Lista de pets (LazyColumn é o "RecyclerView" do Compose)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pets, key = { it.petId }) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = {
                                onPetClick(pet.petId.toString())
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente de UI reutilizável para o card de pet.
 * (Conforme "2. Tela Inicial – Meus Pets")
 */
@Composable
private fun PetCard(pet: Pet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Ação de clique
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Adicionar foto do pet (RF01)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "${pet.especie} (${pet.raca})",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}