package com.example.petcaresistemadecontroleerotinaparapets.data.repository

import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
// ✅ IMPORTS ADICIONADOS
import kotlinx.coroutines.flow.flatMapLatest
// FIM DA ADIÇÃO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepository @Inject constructor(
    private val petDao: PetDao,
    private val authService: FirebaseAuthService
) {

    /**
     * Busca a lista de pets do usuário logado de forma reativa.
     * ✅ CORREÇÃO: Agora usa flatMapLatest para "trocar" o Flow de usuário
     * pelo Flow de pets assim que o usuário fizer login.
     */
    fun getPets(): Flow<List<Pet>> {
        return authService.getUserIdFlow().flatMapLatest { userId ->
            if (userId == null) {
                // Usuário deslogado, emite uma lista vazia
                flowOf(emptyList())
            } else {
                // Usuário logado, emite a lista de pets do banco (que atualiza sozinha)
                petDao.getPetsByUserId(userId)
            }
        }
    }

    // (O resto do arquivo: getPetById, addPet, updatePet, deletePet... pode ficar igual)
    // A função addPet() usa 'getCurrentUserId()', o que está correto,
    // pois o usuário JÁ ESTARÁ logado quando clicar em "salvar".

    suspend fun getPetById(petId: Int): Pet? {
        return petDao.getPetById(petId)
    }

    suspend fun addPet(nome: String, especie: String, raca: String, idade: Int) {
        val userId = authService.getCurrentUserId()
        if (userId != null) {
            val newPet = Pet(
                userId = userId,
                nome = nome,
                especie = especie,
                raca = raca,
                idade = idade,
                isSynced = false
            )
            petDao.insertPet(newPet)
            // TODO: Sincronização com Firestore
        }
    }

    suspend fun updatePet(pet: Pet) {
        petDao.updatePet(pet.copy(isSynced = false))
        // TODO: Sincronização com Firestore
    }

    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
        // TODO: Sincronização com Firestore
    }
}