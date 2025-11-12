package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
// Importa os ViewModels que o AppNavigation vai injetar
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: String?, // Vem da rota como String
    petViewModel: PetViewModel,
    eventoViewModel: EventoViewModel,
    navController: NavController,
    onAddEventClick: () -> Unit
) {
    // 1. Converte o ID para Int para usar no banco
    val petIdInt = petId?.toIntOrNull()

    // 2. Observa as listas dos ViewModels
    // Encontra o pet específico na lista de pets já carregada
    val pets by petViewModel.pets.collectAsState()
    val pet = pets.find { it.idPet == petIdInt }

    // Observa a lista de eventos
    val eventos by eventoViewModel.eventos.collectAsState()

    // 3. Carrega os eventos para este pet específico
    LaunchedEffect(key1 = petIdInt) {
        if (petIdInt != null) {
            eventoViewModel.carregarEventos(petIdInt)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // Mostra o nome do pet (ou "Carregando...")
                title = { Text(pet?.nome ?: "Detalhes do Pet") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddEventClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Evento")
            }
        }
    ) { padding ->
        // 4. Se o pet não for encontrado (carregando ou erro), mostra um aviso
        if (pet == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (petId == null) {
                    Text("Erro: ID do pet não encontrado.")
                } else {
                    CircularProgressIndicator()
                }
            }
        } else {
            // 5. Se o pet for encontrado, exibe os detalhes
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // --- Seção de Detalhes do Pet ---
                Text("Informações do Pet", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
                PetInfoRow(label = "Espécie:", value = pet.especie)
                pet.raca?.let { PetInfoRow(label = "Raça:", value = it) }
                pet.idade?.let { PetInfoRow(label = "Idade:", value = it.toString()) }

                Divider(modifier = Modifier.padding(vertical = 24.dp))

                // --- Seção de Eventos ---
                Text("Próximos Eventos", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))

                if (eventos.isEmpty()) {
                    Text(
                        "Nenhum evento cadastrado para ${pet.nome}. \nClique no '+' para adicionar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(eventos) { evento ->
                            EventCard(evento = evento)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Um Composable privado para exibir uma linha de informação do pet.
 */
@Composable
private fun PetInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(80.dp)
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * Um Composable privado para exibir o card de cada evento.
 */
@Composable
private fun EventCard(evento: Evento) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Event,
                contentDescription = "Evento",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(evento.tipoEvento, style = MaterialTheme.typography.titleMedium)
                Text(evento.dataEvento, style = MaterialTheme.typography.bodyMedium)
                evento.observacoes?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}