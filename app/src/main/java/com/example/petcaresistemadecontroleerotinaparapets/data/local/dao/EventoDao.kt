package com.example.petcaresistemadecontroleerotinaparapets.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento

@Dao
interface EventoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(evento: Evento)

    @Update
    suspend fun update(evento: Evento)

    @Delete
    suspend fun delete(evento: Evento)

    @Query("SELECT * FROM eventos WHERE petId = :petId")
    suspend fun getEventosByPet(petId: Int): List<Evento>

    @Query("SELECT * FROM eventos WHERE isSynced = 0")
    suspend fun getUnsyncedEventos(): List<Evento>
}