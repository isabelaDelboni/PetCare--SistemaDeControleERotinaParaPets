package com.example.petcaresistemadecontroleerotinaparapets.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "eventos",
    foreignKeys = [
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["petId"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Evento(
    @PrimaryKey(autoGenerate = true)
    val idEvento: Int = 0,
    val petId: Int,
    val tipoEvento: String,
    val dataEvento: String,
    val observacoes: String?,
    val isSynced: Boolean = false,

    // ✅✅✅ ADICIONE ESTA LINHA ✅✅✅
    val valor: Double? = null
)