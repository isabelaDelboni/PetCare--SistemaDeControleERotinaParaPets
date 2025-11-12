package com.example.petcaresistemadecontroleerotinaparapets.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val idPet: Int = 0,
    val nome: String,
    val especie: String,
    val raca: String?,
    val idade: Int?,
    val usuarioId: String,         // UID do Firebase Auth
    val urlFoto: String? = null,
    val isSynced: Boolean = false // controle simples de sincronização
)