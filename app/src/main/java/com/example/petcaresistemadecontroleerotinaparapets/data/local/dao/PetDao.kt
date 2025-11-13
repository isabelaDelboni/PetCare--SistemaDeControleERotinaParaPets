package com.example.petcaresistemadecontroleerotinaparapets.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    // Lembre-se: Retornar Long para pegar o ID gerado
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: Pet): Long

    @Update
    suspend fun updatePet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)

    // --- CORREÇÕES NAS QUERIES ABAIXO ---

    // Busca todos os pets de um usuário específico
    @Query("SELECT * FROM pets WHERE userId = :userId")
    fun getPetsDoUsuario(userId: String): Flow<List<Pet>>

    // Busca pets pelo usuário (versão suspend, se precisar)
    @Query("SELECT * FROM pets WHERE userId = :userId")
    suspend fun getPetsByUser(userId: String): List<Pet>

    // Busca um pet específico pelo ID
    // MUDAMOS AQUI: de 'WHERE petId' para 'WHERE idPet'
    @Query("SELECT * FROM pets WHERE idPet = :petId")
    suspend fun getPetById(petId: Int): Pet?

    @Query("SELECT * FROM pets WHERE isSynced = 0")
    suspend fun getUnsyncedPets(): List<Pet>
}