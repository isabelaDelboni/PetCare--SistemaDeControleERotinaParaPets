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

    // ADICIONE VALORES PADRÃO (= "") PARA O FIREBASE NÃO TRAVAR
    val userId: String = "",
    val nome: String = "",
    val especie: String = "",
    val raca: String = "",
    val idade: Int = 0,

    // Campo da foto que fizemos no passo anterior (Storage)
    val urlFoto: String? = null,

    val isSynced: Boolean = false
)