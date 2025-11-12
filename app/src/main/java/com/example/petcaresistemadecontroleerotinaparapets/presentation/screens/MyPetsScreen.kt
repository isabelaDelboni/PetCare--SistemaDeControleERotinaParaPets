package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets // Ícone genérico para pet
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPetsScreen(
    petViewModel: PetViewModel,
    authViewModel: AuthViewModel,
    onPetClick: (String) -> Unit, // O AppNavigation espera o ID como String
    onAddPetClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // 1. Observa o StateFlow do ViewModel. O 'pets' será atualizado automaticamente.
    val pets by petViewModel.pets.collectAsState()
    val currentUser = authViewModel.getCurrentUser()

    // 2. Lógica para carregar os pets do usuário logado
    // Isso é chamado apenas uma vez quando a tela é exibida
    LaunchedEffect(key1 = currentUser) {
        if (currentUser != null) {
            // Chama a função do ViewModel que usa o repositório
            petViewModel.carregarPets(currentUser.uid)
        }
    }

    // 3. Estrutura da UI da tela
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pets") },
                actions = {
                    // Botão de Configurações (para o Logout)
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurações")
                    }
                }
            )
        },
        floatingActionButton = {
            // Botão flutuante para adicionar pets, como no diagrama
            FloatingActionButton(onClick = onAddPetClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Pet")
            }
        }
    ) { padding ->
        // 4. Conteúdo principal (A lista ou o aviso de lista vazia)
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (pets.isEmpty()) {
                // Mensagem de boas-vindas se não houver pets
                Text(
                    text = "Bem-vindo(a)! \nClique no '+' para adicionar seu primeiro pet.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // 5. A lista de pets
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pets) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = {
                                // O ID do Pet é Int no banco,
                                // mas a rota de navegação espera uma String.
                                onPetClick(pet.idPet.toString())
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Um Composable privado para exibir o card de cada pet.
 */
@Composable
private fun PetCard(pet: Pet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Faz o card ser clicável
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Substituir este ícone pela 'pet.urlFoto' quando a lógica de upload (StorageService) for implementada na UI.
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = pet.especie,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Mostra o nome do pet
                Text(pet.nome, style = MaterialTheme.typography.titleLarge)
                // Mostra a raça, ou a espécie se a raça não foi preenchida
                Text(
                    text = pet.raca?.takeIf { it.isNotBlank() } ?: pet.especie,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}