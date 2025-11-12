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
// ✅ IMPORTS DO VICO ADICIONADOS
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.example.petcaresistemadecontroleerotinaparapets.utils.DateConverter
// --- FIM DA ADIÇÃO ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    petId: String?,
    navController: NavController,
    petViewModel: PetViewModel,
    eventoViewModel: EventoViewModel
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

    // --- LÓGICA DO GRÁFICO (RF06) ---

    // 1. Filtra apenas eventos de "Peso" que tenham um valor
    val dadosDePeso = eventos.filter { it.tipoEvento == "Peso" && it.valor != null }

    // 2. Prepara os dados para o gráfico Vico
    // (Converte a lista de eventos de peso em entradas de gráfico)
    val chartEntryModelProducer = ChartEntryModelProducer(
        dadosDePeso.mapNotNull { evento ->
            // Usa o DateConverter para o eixo X
            val timestamp = DateConverter.parseDateToTimestamp(evento.dataEvento)
            if (timestamp != null) {
                // X = Data (como um número), Y = Peso
                entryOf(timestamp.toFloat(), evento.valor!!.toFloat())
            } else {
                null
            }
        }.sortedBy { it.x } // Garante que as datas estejam em ordem
    )

    // 3. Processa os dados de estatísticas (contagem)
    val estatisticasEventos = eventos
        .groupBy { it.tipoEvento }
        .mapValues { it.value.size }
    // --- FIM DA LÓGICA DO GRÁFICO ---


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
            // --- Card de Contagem de Eventos (Existente) ---
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
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                if (dadosDePeso.isEmpty()) {
                    Text(
                        "Nenhum registro de peso encontrado.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    // ✅ GRÁFICO VICO ADICIONADO
                    Chart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        chart = lineChart(),
                        chartModelProducer = chartEntryModelProducer,
                        startAxis = rememberStartAxis(title = "Peso (kg)"),
                        bottomAxis = rememberBottomAxis(
                            title = "Data",
                            // Converte o timestamp (Float) de volta para uma data legível
                            valueFormatter = { value, _ ->
                                // Simplificado - idealmente formataria a data
                                (value.toLong() / 86400000).toString() // Mostra o dia
                            }
                        ),
                    )
                }
            }
        }
    }
}

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