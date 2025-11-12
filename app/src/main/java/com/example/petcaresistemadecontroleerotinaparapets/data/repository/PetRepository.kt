package com.example.petcaresistemadecontroleerotinaparapets.data.repository

import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório para a entidade Pet.
 * Segue o padrão de arquitetura (MVVM + Repository) do seu plano.
 *
 * Responsável por gerenciar a busca, inserção, atualização e exclusão de pets,
 * abstraindo a origem dos dados (Room ou Firebase).
 *
 * @param petDao O DAO para acesso ao banco de dados Room.
 * @param authService O serviço de autenticação para obter o ID do usuário.
 */
@Singleton // Garante que haja apenas uma instância deste repositório no app
class PetRepository @Inject constructor(
    private val petDao: PetDao,
    private val authService: FirebaseAuthService
    // TODO: Futuramente, injetar aqui o serviço do Firebase Firestore
) {

    /**
     * Busca a lista de pets do usuário logado.
     * Retorna um Flow para reatividade (local-first).
     */
    fun getPets(): Flow<List<Pet>> {
        val userId = authService.getCurrentUserId()
        if (userId == null) {
            // Se o usuário não estiver logado, retorna um fluxo vazio.
            return flowOf(emptyList())
        }
        return petDao.getPetsByUserId(userId)
    }

    /**
     * Busca um único pet pelo seu ID.
     */
    suspend fun getPetById(petId: Int): Pet? {
        return petDao.getPetById(petId)
    }

    /**
     * Adiciona um novo pet.
     * Conforme o plano "local-first", ele insere no Room.
     */
    suspend fun addPet(nome: String, especie: String, raca: String, idade: Int) {
        val userId = authService.getCurrentUserId()
        if (userId != null) {
            val newPet = Pet(
                userId = userId,
                nome = nome,
                especie = especie,
                raca = raca,
                idade = idade,
                isSynced = false // Marcar como não sincronizado (para o RF05)
            )
            petDao.insertPet(newPet)

            // TODO: Adicionar lógica de sincronização com o Firestore aqui.
            // Ex: firestoreService.addPet(newPet)
            // Se sucesso -> petDao.updatePet(newPet.copy(isSynced = true))
        }
        // (Você pode querer tratar o 'else' com um erro)
    }

    /**
     * Atualiza um pet existente.
     */
    suspend fun updatePet(pet: Pet) {
        // Marca como não sincronizado para forçar a atualização no Firebase
        petDao.updatePet(pet.copy(isSynced = false))

        // TODO: Adicionar lógica de sincronização com o Firestore
    }

    /**
     * Deleta um pet.
     */
    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)

        // TODO: Adicionar lógica de sincronização com o Firestore (deleção)
    }
}