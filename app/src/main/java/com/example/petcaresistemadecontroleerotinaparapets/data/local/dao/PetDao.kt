package com.example.petcaresistemadecontroleerotinaparapets.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: Pet)

    @Update
    suspend fun update(pet: Pet)

    @Delete
    suspend fun delete(pet: Pet)

    @Query("SELECT * FROM pets WHERE usuarioId = :userId")
    suspend fun getPetsByUser(userId: String): List<Pet>

    @Query("SELECT * FROM pets WHERE isSynced = 0")
    suspend fun getUnsyncedPets(): List<Pet>
}