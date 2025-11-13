package com.example.petcaresistemadecontroleerotinaparapets.data.repository

import android.util.Log
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepository @Inject constructor(
    private val petDao: PetDao,
    private val authService: FirebaseAuthService,
    private val firestoreService: FirestoreService
) {

    suspend fun addPet(pet: Pet) {
        try {
            // 1. Salva no Room e pega o ID
            val localId = petDao.insertPet(pet)
            val petComId = pet.copy(idPet = localId.toInt())

            // 2. Tenta salvar no Firebase
            val userId = authService.getCurrentUserId()
            if (userId != null) {
                val result = firestoreService.savePetRemote(petComId, userId)
                if (result.isSuccess) {
                    petDao.updatePet(petComId.copy(isSynced = true))
                }
            }
        } catch (e: Exception) {
            Log.e("PetRepo", "Erro ao adicionar: ${e.message}")
        }
    }

    suspend fun updatePet(pet: Pet) {
        petDao.updatePet(pet) // Atualiza local

        val userId = authService.getCurrentUserId()
        if (userId != null) {
            try {
                firestoreService.savePetRemote(pet, userId)
                petDao.updatePet(pet.copy(isSynced = true))
            } catch (e: Exception) {
                petDao.updatePet(pet.copy(isSynced = false))
            }
        }
    }

    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
        // Opcional: Implementar delete remoto aqui
    }

    suspend fun getPetById(id: Int): Pet? {
        return petDao.getPetById(id)
    }

    // ✅ ESSA É A FUNÇÃO QUE ESTAVA FALTANDO OU COM NOME ERRADO
    fun getPetsDoUsuario(): Flow<List<Pet>> {
        return authService.getUserIdFlow().flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList())
            } else {
                petDao.getPetsDoUsuario(userId)
            }
        }
    }
}