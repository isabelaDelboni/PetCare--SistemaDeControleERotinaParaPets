package com.example.petcaresistemadecontroleerotinaparapets.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val idUsuario: String, // UID do Firebase Auth
    val nome: String?,
    val email: String?,
    val dataCadastro: String?
)
