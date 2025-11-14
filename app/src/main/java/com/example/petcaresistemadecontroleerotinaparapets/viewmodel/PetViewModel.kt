package com.example.petcaresistemadecontroleerotinaparapets.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.StorageService
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
    private val petRepository: PetRepository,
    private val storageService: StorageService,
    private val authService: FirebaseAuthService
) : ViewModel() {

    // (Para a Lista 'Meus Pets')
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    // (Para a Tela de Detalhes)
    private val _selectedPet = MutableStateFlow<Pet?>(null)
    val selectedPet: StateFlow<Pet?> = _selectedPet.asStateFlow()

    private val _uiState = MutableStateFlow<PetUiState>(PetUiState.Idle)
    val uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    init {
        carregarPets()
    }

    // Função de Adicionar com Foto (que já tínhamos)
    fun adicionarPet(nome: String, especie: String, raca: String, idade: Int, fotoUri: Uri?) {
        viewModelScope.launch {
            _uiState.value = PetUiState.Loading
            val userId = authService.getCurrentUserId()
            if (userId == null) {
                _uiState.value = PetUiState.Error("Usuário não logado!")
                return@launch
            }
            try {
                var urlFotoFinal: String? = null
                if (fotoUri != null) {
                    val uploadResult = storageService.uploadPetPhoto(fotoUri, userId)
                    urlFotoFinal = uploadResult.getOrNull()
                }
                val novoPet = Pet(
                    nome = nome, especie = especie, raca = raca, idade = idade,
                    userId = userId, urlFoto = urlFotoFinal, isSynced = false
                )
                petRepository.addPet(novoPet)
                _uiState.value = PetUiState.Success
            } catch (e: Exception) {
                _uiState.value = PetUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

    // Carrega a lista de pets (Meus Pets)
    fun carregarPets() {
        viewModelScope.launch {
            _uiState.value = PetUiState.Loading
            petRepository.getPetsDoUsuario()
                .catch { e -> _uiState.value = PetUiState.Error(e.message ?: "Erro") }
                .collect { lista ->
                    _pets.value = lista
                    _uiState.value = PetUiState.Success
                }
        }
    }

    // 1. Carrega um pet específico pelo ID e o coloca no StateFlow 'selectedPet'
    fun carregarPetPorId(petId: Int) {
        viewModelScope.launch {
            _uiState.value = PetUiState.Loading
            try {
                _selectedPet.value = petRepository.getPetById(petId)
                _uiState.value = PetUiState.Success
            } catch (e: Exception) {
                _uiState.value = PetUiState.Error(e.message ?: "Erro ao buscar pet")
            }
        }
    }

    // 2. Deleta um pet
    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            try {
                petRepository.deletePet(pet)
                // O 'collect' na tela de lista vai atualizar a UI sozinho
            } catch (e: Exception) {
                _uiState.value = PetUiState.Error(e.message ?: "Erro ao deletar pet")
            }
        }
    }
}

// (Estados da UI - sem mudança)
sealed class PetUiState {
    object Idle : PetUiState()
    object Loading : PetUiState()
    object Success : PetUiState()
    data class Error(val message: String) : PetUiState()
}