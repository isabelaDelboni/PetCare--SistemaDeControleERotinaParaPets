package com.example.petcaresistemadecontroleerotinaparapets.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet

@Database(
    entities = [Pet::class, Evento::class],
    // ✅ ALTERE A VERSÃO DE 1 PARA 2
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun eventoDao(): EventoDao
}