package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
// IMPORT CORRETO (para o ícone "Voltar")
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    // ✅ CORREÇÃO: Estes são os parâmetros que estavam faltando
    petId: String?,
    eventoViewModel: EventoViewModel,
    authViewModel: AuthViewModel,
    onEventSaved: () -> Unit
) {
    // Estados para os campos de entrada
    var tipoEvento by remember { mutableStateOf("") }
    var dataEvento by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val petIdInt = petId?.toIntOrNull()

    // Lista de tipos de evento
    val eventTypes = listOf("Vacina", "Banho", "Consulta", "Medicação", "Passeio", "Alimentação")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Evento") },
                navigationIcon = {
                    IconButton(onClick = onEventSaved) {
                        // CORREÇÃO: Ícone AutoMirrored
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

            // --- Botão Salvar ---
            Button(
                onClick = {
                    if (petIdInt == null) {
                        Toast.makeText(context, "Erro: ID do pet inválido.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (tipoEvento.isBlank() || dataEvento.isBlank()) {
                        Toast.makeText(context, "Tipo e Data são obrigatórios.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val novoEvento = Evento(
                        tipoEvento = tipoEvento,
                        dataEvento = dataEvento,
                        observacoes = observacoes.takeIf { it.isNotBlank() },
                        petId = petIdInt,
                        isSynced = false
                    )

                    eventoViewModel.adicionarEvento(novoEvento)

                    Toast.makeText(context, "Evento '$tipoEvento' salvo!", Toast.LENGTH_SHORT).show()
                    onEventSaved()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Evento")
            }
        }
    }
}