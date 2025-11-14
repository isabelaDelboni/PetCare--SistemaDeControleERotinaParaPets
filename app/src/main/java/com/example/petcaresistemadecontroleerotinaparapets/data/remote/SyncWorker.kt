package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.petcaresistemadecontroleerotinaparapets.data.local.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    // Instancia o Banco de dados Local
    private val db = AppDatabase.getInstance(appContext)
    private val petDao = db.petDao()
    private val eventoDao = db.eventoDao()

    // Instancia o Firestore
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun doWork(): Result {
        return try {
            Log.d("SyncWorker", "Iniciando sincronização em segundo plano...")

            // ------------------------------------------------------------
            // 1. SINCRONIZAR PETS (Room -> Firebase)
            // ------------------------------------------------------------
            val unsyncedPets = petDao.getUnsyncedPets()

            for (pet in unsyncedPets) {
                // Se o pet não tiver userId (erro de dados), pula
                if (pet.userId.isBlank()) continue

                try {
                    // Salva no Firestore usando o ID local como ID do documento
                    firestore.collection("users")
                        .document(pet.userId)
                        .collection("pets")
                        .document(pet.idPet.toString())
                        .set(pet, SetOptions.merge())
                        .await() // Espera terminar

                    // Se deu certo, atualiza o banco local
                    petDao.updatePet(pet.copy(isSynced = true))
                    Log.d("SyncWorker", "Pet ${pet.idPet} sincronizado!")

                } catch (e: Exception) {
                    Log.e("SyncWorker", "Erro ao sincronizar pet ${pet.idPet}", e)
                }
            }

            // ------------------------------------------------------------
            // 2. SINCRONIZAR EVENTOS (Room -> Firebase)
            // ------------------------------------------------------------
            val unsyncedEventos = eventoDao.getUnsyncedEventos()

            for (evento in unsyncedEventos) {
                try {
                    // Busca o Pet no banco para pegar o userId dele.
                    val petDono = petDao.getPetById(evento.petId)

                    if (petDono != null && petDono.userId.isNotBlank()) {
                        firestore.collection("users")
                            .document(petDono.userId)
                            .collection("pets")
                            .document(evento.petId.toString())
                            .collection("events")
                            .document(evento.idEvento.toString())
                            .set(evento, SetOptions.merge())
                            .await()

                        // Atualiza localmente
                        eventoDao.updateEvento(evento.copy(isSynced = true))
                        Log.d("SyncWorker", "Evento ${evento.idEvento} sincronizado!")
                    } else {
                        Log.e("SyncWorker", "Não foi possível achar o dono do evento ${evento.idEvento}")
                    }
                } catch (e: Exception) {
                    Log.e("SyncWorker", "Erro ao sincronizar evento ${evento.idEvento}", e)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Falha geral no Worker", e)
            Result.retry() // Tenta de novo mais tarde se der erro geral
        }
    }
}