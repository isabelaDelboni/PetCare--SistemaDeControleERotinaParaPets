package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.petcaresistemadecontroleerotinaparapets.utils.NotificationScheduler
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    petId: String?,
    eventoId: String?, // ✅ PARÂMETRO ADICIONADO
    eventoViewModel: EventoViewModel,
    onEventSaved: () -> Unit
) {
    var tipoEvento by remember { mutableStateOf("") }
    var dataEvento by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var valorPeso by remember { mutableStateOf("") }

    val context = LocalContext.current

    // LÓGICA DE EDIÇÃO
    val isEditMode = eventoId != null
    val eventoIdInt = eventoId?.toIntOrNull()
    var existingEvento by remember { mutableStateOf<Evento?>(null) }

    // (Carrega dados do evento se estiver em modo de edição)
    LaunchedEffect(eventoIdInt) {
        if (isEditMode && eventoIdInt != null) {
            val evento = eventoViewModel.getEventoParaEdicao(eventoIdInt)
            if (evento != null) {
                existingEvento = evento
                tipoEvento = evento.tipoEvento
                dataEvento = evento.dataEvento
                observacoes = evento.observacoes ?: ""

                // ✅✅✅ CORREÇÃO (Linhas 51, 52) ✅✅✅
                // Agora 'evento.valor' existe
                if (evento.tipoEvento == "Peso" && evento.valor != null) {
                    valorPeso = evento.valor.toString()
                }
            }
        }
    }

    val petIdParaSalvar = existingEvento?.petId ?: petId?.toIntOrNull()

    val eventTypes = listOf("Vacina", "Banho", "Consulta", "Medicação", "Passeio", "Alimentação", "Peso")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Evento" else "Adicionar Evento") },
                navigationIcon = {
                    IconButton(onClick = onEventSaved) {
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ... (Dropdown, Data, Peso, Observações - Sem mudanças aqui) ...

            // --- Seletor de Tipo de Evento (Dropdown) ---
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = tipoEvento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Evento *") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    eventTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                tipoEvento = type
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // --- Campo de Data ---
            OutlinedTextField(
                value = dataEvento,
                onValueChange = { dataEvento = it },
                label = { Text("Data * (ex: DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- CAMPO CONDICIONAL DE PESO (RF06) ---
            AnimatedVisibility(visible = tipoEvento == "Peso") {
                OutlinedTextField(
                    value = valorPeso,
                    onValueChange = { valorPeso = it.replace(",", ".") },
                    label = { Text("Peso (kg) *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    suffix = { Text("kg") }
                )
            }

            // --- Campo de Observações ---
            OutlinedTextField(
                value = observacoes,
                onValueChange = { observacoes = it },
                label = { Text("Observações (Opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            )


            Spacer(modifier = Modifier.height(16.dp))

            // --- Botão Salvar / Atualizar ---
            // --- Botão Salvar / Atualizar ---
            Button(
                onClick = {
                    // Validações básicas
                    if (petIdParaSalvar == null) {
                        Toast.makeText(context, "Erro: ID do pet inválido.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (tipoEvento.isBlank() || dataEvento.isBlank()) {
                        Toast.makeText(context, "Tipo e Data são obrigatórios.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val valorDouble = valorPeso.toDoubleOrNull()
                    if (tipoEvento == "Peso" && valorDouble == null) {
                        Toast.makeText(context, "Por favor, insira um peso válido.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Instancia o agendador
                    val scheduler = NotificationScheduler(context)

                    // Cópia local para Smart Cast
                    val eventoParaSalvar = existingEvento

                    if (isEditMode && eventoParaSalvar != null) {
                        // --- MODO EDIÇÃO ---
                        val eventoAtualizado = eventoParaSalvar.copy(
                            tipoEvento = tipoEvento,
                            dataEvento = dataEvento,
                            observacoes = observacoes.takeIf { it.isNotBlank() },
                            isSynced = false,
                            valor = if (tipoEvento == "Peso") valorDouble else null
                        )

                        // 1. Atualiza no banco
                        eventoViewModel.updateEvento(eventoAtualizado)

                        // 2. Agenda a notificação (usando o objeto que acabamos de criar)
                        scheduler.scheduleEventNotification(eventoAtualizado)

                        Toast.makeText(context, "Evento atualizado e lembrete ajustado!", Toast.LENGTH_SHORT).show()

                    } else {
                        // --- MODO ADIÇÃO ---
                        val novoEvento = Evento(
                            tipoEvento = tipoEvento,
                            dataEvento = dataEvento,
                            observacoes = observacoes.takeIf { it.isNotBlank() },
                            petId = petIdParaSalvar,
                            isSynced = false,
                            valor = if (tipoEvento == "Peso") valorDouble else null
                        )

                        // 1. Salva no banco
                        eventoViewModel.adicionarEvento(novoEvento)

                        // 2. Agenda a notificação
                        scheduler.scheduleEventNotification(novoEvento)

                        Toast.makeText(context, "Evento salvo e lembrete criado!", Toast.LENGTH_SHORT).show()
                    }

                    onEventSaved()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Atualizar Evento" else "Salvar Evento")
            }
        }
    }
}