package com.example.petcaresistemadecontroleerotinaparapets.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.EventoDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.PetDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.dao.UsuarioDao
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Pet
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Usuario

/**
 * Banco de dados Room principal do aplicativo.
 *
 * ATUALIZAÇÃO:
 * - Adicionado 'Pet::class' à lista de entidades.
 * - Adicionada a função abstrata 'petDao()'.
 * - Versão do banco incrementada para 2 (necessário ao alterar o schema).
 */
@Database(
    entities = [
        Usuario::class,
        Pet::class, // <-- ENTIDADE ADICIONADA
        Evento::class
    ],
    version = 2, // <-- VERSÃO INCREMENTADA
    exportSchema = false // (Mantido do seu log de build, para suprimir o aviso)
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun eventoDao(): EventoDao
    abstract fun petDao(): PetDao // <-- DAO ADICIONADO

}