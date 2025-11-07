package com.example.petcaresistemadecontroleerotinaparapets.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventos")
data class Evento(
    @PrimaryKey(autoGenerate = true)
    val idEvento: Int = 0,
    val tipoEvento: String,
    val dataEvento: String,       // ISO string ou timestamp como preferir
    val observacoes: String?,
    val petId: Int,
    val isSynced: Boolean = false
)
