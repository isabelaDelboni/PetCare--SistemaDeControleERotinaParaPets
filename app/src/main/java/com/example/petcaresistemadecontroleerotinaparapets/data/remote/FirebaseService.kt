package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import android.util.Log
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject // <--- IMPORTANTE

// ADICIONADO: @Inject constructor()
class FirestoreService @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    // --- PETS ---

    suspend fun savePetRemote(pet: Pet, userId: String): Result<Boolean> {
        return try {
            val documentRef = if (pet.idPet != 0) {
                db.collection("users").document(userId)
                    .collection("pets").document(pet.idPet.toString())
            } else {
                db.collection("users").document(userId)
                    .collection("pets").document()
            }

            documentRef.set(pet, SetOptions.merge()).await()
            Log.d("FirestoreService", "Pet salvo com sucesso: ${documentRef.id}")
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Erro ao salvar pet", e)
            Result.failure(e)
        }
    }

    suspend fun getPetsRemote(userId: String): Result<List<Pet>> {
        return try {
            val snapshot = db.collection("users").document(userId)
                .collection("pets")
                .get().await()

            val pets = snapshot.toObjects(Pet::class.java)
            Result.success(pets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- EVENTOS ---

    suspend fun saveEventoRemote(evento: Evento, userId: String, petId: Int): Result<Boolean> {
        return try {
            val documentRef = db.collection("users").document(userId)
                .collection("pets").document(petId.toString())
                .collection("events").document(evento.idEvento.toString())

            documentRef.set(evento, SetOptions.merge()).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}