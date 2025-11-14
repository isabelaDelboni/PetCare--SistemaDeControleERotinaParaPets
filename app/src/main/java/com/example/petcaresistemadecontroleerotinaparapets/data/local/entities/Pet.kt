package com.example.petcaresistemadecontroleerotinaparapets.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pets",
    indices = [Index(value = ["userId"])]
)
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val idPet: Int = 0,

    val userId: String = "",
    val nome: String = "",
    val especie: String = "",
    val raca: String = "",
    val idade: Int = 0,

    val urlFoto: String? = null,

    val isSynced: Boolean = false
)