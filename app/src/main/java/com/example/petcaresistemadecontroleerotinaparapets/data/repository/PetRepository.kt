package com.example.petcaresistemadecontroleerotinaparapets.data.repository

import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.google.firebase.firestore.FirebaseFirestore

class PetRepository(
    private val petDao: PetDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun addPet(pet: Pet) {
        // salva local
        petDao.insert(pet.copy(isSynced = false))

        // tenta salvar remoto (não bloqueante)
        try {
            val docRef = firestore.collection("users")
                .document(pet.usuarioId)
                .collection("pets")
                .document() // gera id no Firestore
            // se quiser manter idPet = docRef.id, ajuste lógica
            docRef.set(pet).addOnSuccessListener {
                // ideal: atualizar o pet local marcando isSynced = true (precisa obter id local)
            }.addOnFailureListener { /* log */ }
        } catch (e: Exception) {
            // sem internet: ok — ficará no Room para sincronizar depois
        }
    }

    suspend fun getPetsByUser(userId: String): List<Pet> =
        petDao.getPetsByUser(userId)

    suspend fun getUnsyncedPets(): List<Pet> = petDao.getUnsyncedPets()

    suspend fun markPetAsSynced(localId: Int) {
        petDao.getPetsByUser("") // exemplo: buscar e atualizar. Melhor implementar uma query para atualizar por id
        // Recomendo criar método no DAO para atualizar isSynced por id
    }
}
