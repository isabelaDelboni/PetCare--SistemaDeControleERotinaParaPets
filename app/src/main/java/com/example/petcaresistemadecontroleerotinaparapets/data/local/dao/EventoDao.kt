package com.example.petcaresistemadecontroleerotinaparapets.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvento(evento: Evento): Long

    @Update
    suspend fun updateEvento(evento: Evento)

    @Delete
    suspend fun deleteEvento(evento: Evento)

    // Busca eventos de um pet específico
    @Query("SELECT * FROM eventos WHERE petId = :petId ORDER BY dataEvento DESC")
    fun getEventosDoPet(petId: Int): Flow<List<Evento>>

    @Query("SELECT * FROM eventos WHERE idEvento = :id")
    suspend fun getEventoById(id: Int): Evento?

    @Query("SELECT * FROM eventos WHERE isSynced = 0")
    suspend fun getUnsyncedEventos(): List<Evento>

    /**
     * Busca todos os eventos de todos os pets de um usuário específico.
     */
    @Query("""
        SELECT E.* FROM eventos E 
        INNER JOIN pets P ON E.petId = P.idPet 
        WHERE P.userId = :userId 
        ORDER BY E.dataEvento ASC
    """)
    fun getAllEventosDoUsuario(userId: String): Flow<List<Evento>>
}