package com.example.petcaresistemadecontroleerotinaparapets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.repository.EventoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventoViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    fun carregarEventos(petId: Int) {
        viewModelScope.launch {
            _eventos.value = eventoRepository.getEventosByPet(petId)
        }
    }

    fun adicionarEvento(evento: Evento) {
        viewModelScope.launch {
            eventoRepository.addEvento(evento)
            carregarEventos(evento.petId)
        }
    }
}