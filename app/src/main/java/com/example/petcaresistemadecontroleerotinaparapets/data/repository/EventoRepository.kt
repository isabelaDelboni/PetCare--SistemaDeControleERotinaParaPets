package com.example.petcaresistemadecontroleerotinaparapets.data.repository

import android.util.Log
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirebaseAuthService
import com.example.petcaresistemadecontroleerotinaparapets.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventoRepository @Inject constructor(
    private val eventoDao: EventoDao,
    private val petDao: PetDao, // Precisamos do PetDao para confirmar donos se necessário
    private val firestoreService: FirestoreService,
    private val authService: FirebaseAuthService
) {

    suspend fun adicionarEvento(evento: Evento) {
        try {
            // 1. Salva no Room (Offline garantido)
            val localId = eventoDao.insertEvento(evento)
            val eventoSalvo = evento.copy(idEvento = localId.toInt())
            Log.d("EventoRepo", "Evento salvo localmente ID: $localId")

            // 2. Tenta sincronizar com Firebase
            val userId = authService.getCurrentUserId()

            // Só tenta enviar se tiver usuário logado
            if (userId != null) {
                // Precisamos do petId para o caminho no Firestore
                val result = firestoreService.saveEventoRemote(eventoSalvo, userId, evento.petId)

                if (result.isSuccess) {
                    // 3. Sucesso: Marca como sincronizado no Room
                    eventoDao.updateEvento(eventoSalvo.copy(isSynced = true))
                    Log.d("EventoRepo", "Evento sincronizado com sucesso!")
                } else {
                    Log.e("EventoRepo", "Falha no envio para Firebase (sem internet?)")
                }
            }
        } catch (e: Exception) {
            Log.e("EventoRepo", "Erro ao adicionar evento: ${e.message}")
        }
    }

    suspend fun updateEvento(evento: Evento) {
        // Atualiza local
        eventoDao.updateEvento(evento)

        // Tenta remoto
        val userId = authService.getCurrentUserId()
        if (userId != null) {
            try {
                val result = firestoreService.saveEventoRemote(evento, userId, evento.petId)
                if (result.isSuccess) {
                    eventoDao.updateEvento(evento.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Se falhar, garante que está marcado como false para o SyncWorker pegar depois
                eventoDao.updateEvento(evento.copy(isSynced = false))
            }
        }
    }

    suspend fun excluirEvento(evento: Evento) {
        eventoDao.deleteEvento(evento)
        // Implementar delete no FirestoreService se necessário no futuro
    }

    suspend fun getEventoById(id: Int): Evento? {
        return eventoDao.getEventoById(id)
    }

    fun getEventosDoPet(petId: Int): Flow<List<Evento>> {
        return eventoDao.getEventosDoPet(petId)
    }

    // Busca todos os eventos do usuário logado (para a tela de Lembretes/Agenda geral)
    fun getAllEventosDoUsuario(): Flow<List<Evento>> {
        return authService.getUserIdFlow().flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList())
            } else {
                eventoDao.getAllEventosDoUsuario(userId)
            }
        }
    }
}