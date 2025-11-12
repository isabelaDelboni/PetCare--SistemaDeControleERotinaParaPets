package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart // <-- ÍCONE ADICIONADO
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.presentation.navigation.ScreenRoutes // <-- IMPORT ADICIONADO
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: String?,
    navController: NavController,
    petViewModel: PetViewModel,
    eventoViewModel: EventoViewModel,
    onAddEventClick: () -> Unit
) {
    val petIdInt = petId?.toIntOrNull()
    val pet by petViewModel.selectedPet.collectAsState()
    val eventos by eventoViewModel.eventos.collectAsState()

    LaunchedEffect(petIdInt) {
        if (petIdInt != null) {
            petViewModel.carregarPetPorId(petIdInt)
            eventoViewModel.carregarEventosDoPet(petIdInt)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pet?.nome ?: "Detalhes do Pet") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                // --- BOTÃO DE RELATÓRIO ADICIONADO ---
                actions = {
                    IconButton(onClick = {
                        if (petId != null) {
                            navController.navigate(ScreenRoutes.reports(petId))
                        }
                    }) {
                        Icon(Icons.Default.BarChart, contentDescription = "Relatórios do Pet")
                    }
                }
                // --- FIM DA ADIÇÃO ---
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddEventClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Evento")
            }
        }
    ) { padding ->
        // ... (O resto da tela (LazyColumn) permanece o mesmo) ...
        if (pet == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    PetInfoCard(pet!!)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Próximos Eventos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (eventos.isEmpty()) {
                    item {
                        Text(
                            "Nenhum evento cadastrado para este pet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(eventos, key = { it.eventoId }) { evento ->
                        EventoCard(evento)
                    }
                }
            }
        }
    }
}

// ... (PetInfoCard, EventoCard, InfoLinha permanecem os mesmos) ...
@Composable
private fun PetInfoCard(pet: Pet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = pet.nome,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            InfoLinha(label = "Espécie:", valor = pet.especie)
            InfoLinha(label = "Raça:", valor = pet.raca)
            InfoLinha(label = "Idade:", valor = "${pet.idade} anos")
        }
    }
}

@Composable
private fun EventoCard(evento: Evento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = evento.tipoEvento,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (evento.observacoes != null) {
                    Text(
                        text = evento.observacoes,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = evento.dataEvento,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun InfoLinha(label: String, valor: String) {
    Row {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(80.dp)
        )
        Text(text = valor)
    }
}