package com.example.petcaresistemadecontroleerotinaparapets.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.repository.EventoRepository
import com.example.petcaresistemadecontroleerotinaparapets.utils.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventoViewModel @Inject constructor(
    private val eventoRepository: EventoRepository,
    @ApplicationContext private val context: Context // <-- INJETANDO O CONTEXTO
) : ViewModel() {

    // --- ADICIONADO ---
    // Instancia o Agendador de Notificações
    private val scheduler = NotificationScheduler(context)
    // --- FIM DA ADIÇÃO ---

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos.asStateFlow()

    private val _todosOsEventos = MutableStateFlow<List<Evento>>(emptyList())
    val todosOsEventos: StateFlow<List<Evento>> = _todosOsEventos.asStateFlow()

    private val _uiState = MutableStateFlow<EventoUiState>(EventoUiState.Idle)
    val uiState: StateFlow<EventoUiState> = _uiState.asStateFlow()

    /**
     * Adiciona um novo evento E AGENDA A NOTIFICAÇÃO (RF04).
     */
    fun adicionarEvento(evento: Evento) {
        viewModelScope.launch {
            eventoRepository.adicionarEvento(evento)

            // --- LÓGICA DE AGENDAMENTO (RF04) ---
            scheduler.scheduleNotification(evento)
            // --- FIM DA ADIÇÃO ---
        }
    }

    fun carregarEventosDoPet(petId: Int) {
        viewModelScope.launch {
            _uiState.value = EventoUiState.Loading
            eventoRepository.getEventosDoPet(petId)
                .catch { e ->
                    _uiState.value = EventoUiState.Error(e.message ?: "Erro ao carregar eventos")
                }
                .collect { listaDeEventos ->
                    _eventos.value = listaDeEventos
                    _uiState.value = EventoUiState.Success
                }
        }
    }

    fun carregarTodosOsEventos() {
        viewModelScope.launch {
            _uiState.value = EventoUiState.Loading
            // (Esta função ainda precisa ser criada no EventoRepository)
            // eventoRepository.getAllEventosDoUsuario()
            //     .catch { ... }
            //     .collect { ... }
        }
    }
}

// (Classe de estado da UI permanece a mesma)
sealed class EventoUiState {
    object Idle : EventoUiState()
    object Loading : EventoUiState()
    object Success : EventoUiState()
    data class Error(val message: String) : EventoUiState()
}