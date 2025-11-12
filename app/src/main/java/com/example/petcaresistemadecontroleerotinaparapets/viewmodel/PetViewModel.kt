package com.example.petcaresistemadecontroleerotinaparapets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. CORREÇÃO: O nome da classe deve ser PetViewModel
@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    // 2. CORREÇÃO: A lógica deve ser sobre _pets, não _eventos
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets

    fun carregarPets(userId: String) {
        viewModelScope.launch {
            val list = petRepository.getPetsByUser(userId)
            _pets.value = list
        }
    }

    fun adicionarPet(pet: Pet) {
        viewModelScope.launch {
            petRepository.addPet(pet)
            carregarPets(pet.usuarioId)
        }
    }
}