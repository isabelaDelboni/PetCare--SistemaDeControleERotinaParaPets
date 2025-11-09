package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.petcaresistemadecontroleerotinaparapets.data.local.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val db = AppDatabase.getInstance(appContext)
    private val petDao = db.petDao()
    private val eventoDao = db.eventoDao()
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun doWork(): Result {
        try {
            // Pets
            val unsyncedPets = petDao.getUnsyncedPets()
            unsyncedPets.forEach { pet ->
                val docRef = firestore.collection("users")
                    .document(pet.usuarioId)
                    .collection("pets")
                    .document(pet.idPet.toString()) // uso do id local como id remoto (pode ajustar)
                docRef.set(pet).addOnSuccessListener {
                    // marcar como synced: você precisa de um DAO para isso; implementar updateIsSynced(id, true)
                }.addOnFailureListener {
                    // log
                }
            }

            // Eventos
            val unsyncedEventos = eventoDao.getUnsyncedEventos()
            unsyncedEventos.forEach { evento ->
                // precisamos saber o usuarioId para construir caminho no Firestore.
                // se não tiver, pode manter em /events-global ou salvar userId no evento
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}
