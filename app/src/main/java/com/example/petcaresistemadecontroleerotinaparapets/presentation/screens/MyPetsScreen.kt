package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.foundation.background
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
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPetsScreen(
    petViewModel: PetViewModel,
    authViewModel: AuthViewModel,
    onPetClick: (String) -> Unit,
    onAddPetClick: () -> Unit,
    onEditPetClick: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    val pets by petViewModel.pets.collectAsState()
    val uiState by petViewModel.uiState.collectAsState()

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
        MyPetsContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            pets = pets,
            onPetClick = onPetClick,
            onEditPetClick = onEditPetClick
        )
    }
}

@Composable
private fun MyPetsContent(
    modifier: Modifier = Modifier,
    uiState: PetUiState,
    pets: List<Pet>,
    onPetClick: (String) -> Unit,
    onEditPetClick: (String) -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is PetUiState.Idle,
            is PetUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PetUiState.Success -> {
                if (pets.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Você ainda não adicionou nenhum pet.")
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // CORREÇÃO 1: key = { it.idPet } (era it.petId)
                        items(pets, key = { it.idPet }) { pet ->
                            PetCard(
                                pet = pet,
                                // CORREÇÃO 2: pet.idPet (era pet.petId)
                                onClick = { onPetClick(pet.idPet.toString()) },
                                // CORREÇÃO 3: pet.idPet (era pet.petId)
                                onEditClick = { onEditPetClick(pet.idPet.toString()) }
                            )
                        }
                    }
                }
            }
            is PetUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Erro ao carregar pets: ${uiState.message}")
                }
            }
        }
    }
}

@Composable
private fun PetCard(
    pet: Pet,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), // Espaçamento lateral
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Sombra mais forte
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Ou uma cor específica tipo Color.White
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp) // Bordas bem redondinhas
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface) // Garante fundo branco/cor certa
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar do Pet (Círculo colorido)
            Surface(
                modifier = Modifier.size(60.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Textos
            Column(modifier = Modifier.weight(1f).clickable(onClick = onClick)) {
                Text(
                    text = pet.nome,
                    style = MaterialTheme.typography.titleLarge, // Fonte maior
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${pet.especie} • ${pet.raca}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Exemplo de chip de idade
                Text(
                    text = "${pet.idade} anos",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Botão de Editar (Discreto)
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}