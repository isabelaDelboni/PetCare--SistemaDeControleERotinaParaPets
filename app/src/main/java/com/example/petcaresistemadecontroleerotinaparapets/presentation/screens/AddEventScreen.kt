package com.example.petcaresistemadecontroleerotinaparapets.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility // ✅ IMPORT ADICIONADO
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions // ✅ IMPORT ADICIONADO
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType // ✅ IMPORT ADICIONADO
import androidx.compose.ui.unit.dp
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.AuthViewModel
import com.example.petcaresistemadecontroleerotinaparapets.viewmodel.EventoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    petId: String?,
    eventoViewModel: EventoViewModel,
    authViewModel: AuthViewModel,
    onEventSaved: () -> Unit
) {
    var tipoEvento by remember { mutableStateOf("") }
    var dataEvento by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // ✅ ESTADO ADICIONADO
    var valorPeso by remember { mutableStateOf("") }

    val context = LocalContext.current
    val petIdInt = petId?.toIntOrNull()

    // ✅ "Peso" ADICIONADO À LISTA
    val eventTypes = listOf("Vacina", "Banho", "Consulta", "Medicação", "Passeio", "Alimentação", "Peso")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Evento") },
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

            // ✅ --- CAMPO CONDICIONAL DE PESO (RF06) ---
            AnimatedVisibility(visible = tipoEvento == "Peso") {
                OutlinedTextField(
                    value = valorPeso,
                    onValueChange = { valorPeso = it.replace(",", ".") },
                    label = { Text("Peso (kg) *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp), // Espaçamento extra
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    suffix = { Text("kg") }
                )
            }
            // --- FIM DA ADIÇÃO ---

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

                    // ✅ LÓGICA DE VALIDAÇÃO DO PESO
                    val valorDouble = valorPeso.toDoubleOrNull()
                    if (tipoEvento == "Peso" && valorDouble == null) {
                        Toast.makeText(context, "Por favor, insira um peso válido.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    // --- FIM DA ADIÇÃO ---

                    val novoEvento = Evento(
                        tipoEvento = tipoEvento,
                        dataEvento = dataEvento,
                        observacoes = observacoes.takeIf { it.isNotBlank() },
                        petId = petIdInt,
                        isSynced = false,
                        // ✅ ADICIONADO O VALOR
                        valor = if (tipoEvento == "Peso") valorDouble else null
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