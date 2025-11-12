package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.PetViewModel

/**
 * Tela de Relatórios (RF06).
 * Mostra estatísticas para um pet específico.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    petId: String?,
    navController: NavController,
    petViewModel: PetViewModel,
    eventoViewModel: EventoViewModel
) {
    val petIdInt = petId?.toIntOrNull()

    // Observa os dados dos ViewModels
    val pet by petViewModel.selectedPet.collectAsState()
    val eventos by eventoViewModel.eventos.collectAsState()

    // Carrega os dados (mesmo que PetDetailScreen, para garantir que estejam prontos)
    LaunchedEffect(petIdInt) {
        if (petIdInt != null) {
            petViewModel.carregarPetPorId(petIdInt)
            eventoViewModel.carregarEventosDoPet(petIdInt)
        }
    }

    // Processa os dados para estatísticas (RF06)
    val estatisticasEventos = eventos
        .groupBy { it.tipoEvento } // Agrupa por tipo (ex: "Banho", "Vacina")
        .mapValues { it.value.size } // Conta quantos de cada tipo (ex: "Banho" = 5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de ${pet?.nome ?: "..."}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Card de Contagem de Eventos (RF06) ---
            Text(
                "Resumo de Cuidados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (estatisticasEventos.isEmpty()) {
                        Text("Nenhum evento registrado ainda.")
                    } else {
                        // Lista as estatísticas
                        estatisticasEventos.forEach { (tipo, contagem) ->
                            EstatisticaRow(tipo, contagem.toString())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Card de Gráfico de Peso (RF06) ---
            Text(
                "Histórico de Peso",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Altura fixa para o gráfico
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "TODO: Implementar gráfico de peso (MPAndroidChart ou Compose Charts)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // TODO: Para implementar isso, seria necessário adicionar "Peso"
                    // como um tipo de evento ou um campo especial no 'Evento.kt'.
                }
            }
        }
    }
}

/**
 * Componente helper para a linha "Tipo: Contagem"
 */
@Composable
private fun EstatisticaRow(label: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}