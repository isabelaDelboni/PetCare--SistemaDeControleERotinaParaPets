package com.example.petcaresistemadecontroleerotinaparapets.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.UsuarioDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Usuario

@Database(
    entities = [
        Usuario::class,
        Pet::class,
        Evento::class
    ],
    // ✅ VERSÃO INCREMENTADA DE 2 PARA 3 (devido à adição do campo 'valor' no Evento)
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun eventoDao(): EventoDao
    abstract fun petDao(): PetDao

}