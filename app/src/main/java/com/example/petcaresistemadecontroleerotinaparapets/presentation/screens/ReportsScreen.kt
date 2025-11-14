package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

// Desistido de implementar, deixado para possível implementação futura.

/*import androidx.compose.foundation.layout.Arrangement
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
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.example.petcaresistemadecontroleerotinaparapets.utils.DateConverter
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet


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

    val dadosDePeso = eventos.filter { it.tipoEvento == "Peso" && it.valor != null }

    val chartEntryModelProducer = ChartEntryModelProducer(
        dadosDePeso.mapNotNull { evento ->
            val timestamp = DateConverter.parseDateToTimestamp(evento.dataEvento)
            if (timestamp != null) {
                entryOf(timestamp.toFloat(), evento.valor!!.toFloat())
            } else {
                null
            }
        }.sortedBy { it.x }
    )

    val estatisticasEventos = eventos
        .groupBy { it.tipoEvento }
        .mapValues { it.value.size }


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
                            valueFormatter = { value, _ ->
                                // TODO: Formatar a data corretamente
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
}*/