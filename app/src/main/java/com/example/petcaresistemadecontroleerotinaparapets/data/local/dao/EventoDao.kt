package com.example.petcaresistemadecontroleerotinaparapets.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update // ✅ IMPORT ADICIONADO
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvento(evento: Evento)

    // ✅ FUNÇÃO ADICIONADA
    @Update
    suspend fun updateEvento(evento: Evento)

    @Delete
    suspend fun deleteEvento(evento: Evento)

    @Query("SELECT * FROM eventos WHERE petId = :petId ORDER BY dataEvento DESC")
    fun getEventosDoPet(petId: Int): Flow<List<Evento>>

    // ✅ FUNÇÃO ADICIONADA
    @Query("SELECT * FROM eventos WHERE idEvento = :eventoId")
    suspend fun getEventoById(eventoId: Int): Evento?

    /**
     * Busca todos os eventos de todos os pets de um usuário específico.
     * Usa um JOIN com a tabela 'pets' para filtrar pelo 'userId'.
     */
    @Query("""
        SELECT E.* FROM eventos E 
        INNER JOIN pets P ON E.petId = P.petId 
        WHERE P.userId = :userId 
        ORDER BY E.dataEvento ASC
    """)
    fun getAllEventosDoUsuario(userId: String): Flow<List<Evento>>
}