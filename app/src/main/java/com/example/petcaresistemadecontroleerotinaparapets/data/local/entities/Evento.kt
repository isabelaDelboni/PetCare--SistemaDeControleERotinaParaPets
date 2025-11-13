package com.example.petcaresistemadecontroleerotinaparapets.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "eventos",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["idPet"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["petId"])]
)
data class Evento(
    @PrimaryKey(autoGenerate = true)
    val idEvento: Int = 0,

    // ADICIONE VALORES PADRÃO PARA TODOS (para o Firebase não travar)
    val tipoEvento: String = "",
    val dataEvento: String = "",
    val observacoes: String? = null,
    val petId: Int = 0,

    // --- NOVO CAMPO (Para guardar o Peso ou Valor da consulta) ---
    val valor: Double? = null, // <--- ADICIONE ESTA LINHA

    val isSynced: Boolean = false
)