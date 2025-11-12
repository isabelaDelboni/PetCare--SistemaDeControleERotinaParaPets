package com.example.petcaresistemadecontroleerotinaparapets.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Define a entidade 'Pet' para o banco de dados Room.
 * Baseado no seu plano (RF01 e Entidades).
 *
 * @param petId Chave primária autogerada.
 * @param userId Chave estrangeira que liga ao usuário (Firebase Auth UID).
 * @param nome Nome do pet.
 * @param especie Espécie (ex: "Cachorro", "Gato").
 * @param raca Raça do pet.
 * @param idade Idade do pet.
 * @param isSynced Flag para controle de sincronização com o Firestore.
 */
@Entity(
    tableName = "pets",
    // Criamos um índice na userId para acelerar as consultas de "buscar pets do usuário"
    indices = [Index(value = ["userId"])]
    // NOTA: A Foreign Key para um "Usuario" local foi omitida
    // pois usaremos o Firebase Auth UID (String) como link direto,
    // conforme o plano de sincronização /users/{userId}/pets
)
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val petId: Int = 0,
    val userId: String, // ID do Firebase Auth
    val nome: String,
    val especie: String,
    val raca: String,
    val idade: Int,
    val isSynced: Boolean = false // Padrão 'false' para novos pets
)