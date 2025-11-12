package com.example.petcaresistemadecontroleerotinaparapets.data.repository

import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.google.firebase.firestore.FirebaseFirestore

class EventoRepository(
    private val eventoDao: EventoDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun addEvento(evento: Evento) {
        eventoDao.insert(evento.copy(isSynced = false))
        try {
            firestore.collection("users")
                .document(/* precisa do usuarioId; passe via evento ou contexto */ "uid")
                .collection("pets")
                .document(evento.petId.toString())
                .collection("events")
                .add(evento)
        } catch (e: Exception) {
            // sem internet -> fica no Room
        }
    }

    suspend fun getEventosByPet(petId: Int): List<Evento> =
        eventoDao.getEventosByPet(petId)

    suspend fun getUnsyncedEventos(): List<Evento> =
        eventoDao.getUnsyncedEventos()
}
