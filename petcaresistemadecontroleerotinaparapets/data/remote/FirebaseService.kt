package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import android.util.Log
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    // --- PETS ---

    // Salva ou atualiza um Pet diretamente no Firestore
    suspend fun savePetRemote(pet: Pet, userId: String): Result<Boolean> {
        return try {
            // Se o pet já tiver um ID (viedo do Room, por exemplo), usamos ele.
            // Se for 0 (novo pet ainda não salvo no Room), deixamos o Firestore gerar um ID único.
            val documentRef = if (pet.idPet != 0) {
                db.collection("users").document(userId)
                    .collection("pets").document(pet.idPet.toString())
            } else {
                db.collection("users").document(userId)
                    .collection("pets").document() // Firestore gera o ID
            }

            documentRef.set(pet, SetOptions.merge()).await()
            Log.d("FirestoreService", "Pet salvo com sucesso: ${documentRef.id}")
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Erro ao salvar pet", e)
            Result.failure(e)
        }
    }

    // Busca todos os pets do usuário no Firestore
    suspend fun getPetsRemote(userId: String): Result<List<Pet>> {
        return try {
            val snapshot = db.collection("users").document(userId)
                .collection("pets")
                .get().await()

            // Converte os documentos do Firestore de volta para objetos Pet
            val pets = snapshot.toObjects(Pet::class.java)
            Result.success(pets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- EVENTOS ---

    // Salva um evento DENTRO da subcoleção do Pet correto
    suspend fun saveEventoRemote(evento: Evento, userId: String, petId: Int): Result<Boolean> {
        return try {
            // Caminho: users/{userId}/pets/{petId}/events/{eventoId}
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