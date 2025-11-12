package com.example.petcaresistemadecontroleerotinaparapets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    // --- Lista de Pets (para MyPetsScreen) ---
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    // --- PET SELECIONADO (ADICIONADO) ---
    // (Para PetDetailScreen)
    private val _selectedPet = MutableStateFlow<Pet?>(null)
    val selectedPet: StateFlow<Pet?> = _selectedPet.asStateFlow()
    // --- FIM DA ADIÇÃO ---

    private val _uiState = MutableStateFlow<PetUiState>(PetUiState.Idle)
    val uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    init {
        carregarPets()
    }

    private fun carregarPets() {
        viewModelScope.launch {
            _uiState.value = PetUiState.Loading
            petRepository.getPets()
                .catch { e ->
                    _uiState.value = PetUiState.Error(e.message ?: "Erro ao carregar pets")
                }
                .collect { petList ->
                    _pets.value = petList
                    _uiState.value = PetUiState.Success
                }
        }
    }

    // --- FUNÇÃO ADICIONADA ---
    /**
     * Carrega um pet específico pelo ID.
     * Chamado pela 'PetDetailScreen'.
     */
    fun carregarPetPorId(petId: Int) {
        viewModelScope.launch {
            _selectedPet.value = petRepository.getPetById(petId)
        }
    }
    // --- FIM DA ADIÇÃO ---

    fun addPet(nome: String, especie: String, raca: String, idadeStr: String) {
        viewModelScope.launch {
            val idade = idadeStr.toIntOrNull() ?: 0
            if (nome.isBlank() || especie.isBlank()) {
                _uiState.value = PetUiState.Error("Nome e espécie são obrigatórios.")
                return@launch
            }

            petRepository.addPet(nome, especie, raca, idade)
        }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            petRepository.deletePet(pet)
        }
    }

    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            petRepository.updatePet(pet)
        }
    }
}

// (Classe de estado da UI permanece a mesma)
sealed class PetUiState {
    object Idle : PetUiState()
    object Loading : PetUiState()
    object Success : PetUiState()
    data class Error(val message: String) : PetUiState()
}