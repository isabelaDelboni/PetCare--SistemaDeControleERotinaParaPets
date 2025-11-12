package com.example.petcaresistemadecontroleerotinaparapets.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para a entidade Pet.
 * Define as operações de banco de dados (CRUD) para pets.
 */
@Dao
interface PetDao {

    /**
     * Insere um novo pet no banco.
     * Se o pet já existir (conflito de Primary Key), ele será substituído.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)

    /**
     * Atualiza um pet existente no banco.
     */
    @Update
    suspend fun updatePet(pet: Pet)

    /**
     * Deleta um pet do banco.
     */
    @Delete
    suspend fun deletePet(pet: Pet)

    /**
     * Busca um pet específico pelo seu ID.
     */
    @Query("SELECT * FROM pets WHERE petId = :petId")
    suspend fun getPetById(petId: Int): Pet?

    /**
     * Busca todos os pets de um usuário específico.
     * Retorna um Flow para que a UI seja atualizada automaticamente
     * sempre que os dados dos pets mudarem (reatividade).
     */
    @Query("SELECT * FROM pets WHERE userId = :userId ORDER BY nome ASC")
    fun getPetsByUserId(userId: String): Flow<List<Pet>>

    /**
     * (Para o SyncWorker) Busca todos os pets que ainda não foram sincronizados.
     */
    @Query("SELECT * FROM pets WHERE isSynced = 0") // 0 = false
    suspend fun getUnsyncedPets(): List<Pet>
}