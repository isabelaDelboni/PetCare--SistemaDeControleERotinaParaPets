package com.example.petcaresistemadecontroleerotinaparapets.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE idUsuario = :uid")
    suspend fun getUsuario(uid: String): Usuario?
}
