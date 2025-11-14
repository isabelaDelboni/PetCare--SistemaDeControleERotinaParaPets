package com.example.petcaresistemadecontroleerotinaparapets.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.UsuarioDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Usuario

@Database(
    entities = [Usuario::class, Pet::class, Evento::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun petDao(): PetDao
    abstract fun eventoDao(): EventoDao

    // Singleton para acessar o banco sem injeção de dependência (necessário para o SyncWorker)
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "petcare_database"
                )
                    .fallbackToDestructiveMigration() // Apaga o banco se mudar a versão 
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}