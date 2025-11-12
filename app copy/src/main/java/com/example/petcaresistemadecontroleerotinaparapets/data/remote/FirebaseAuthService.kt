package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
// ✅ IMPORTS ADICIONADOS
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
// FIM DA ADIÇÃO

class FirebaseAuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // (As funções getCurrentUser, getCurrentUserId, signIn, signUp, signOut permanecem iguais)
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
    suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun signOut() {
        auth.signOut()
    }
    // ✅ FUNÇÃO ADICIONADA
    /**
     * Observa o estado de autenticação em tempo real.
     * Emite o UID do usuário (String) ou null se deslogado.
     */
    fun getUserIdFlow(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            // Tenta enviar o UID do usuário atual
            trySend(auth.currentUser?.uid)
        }
        // Adiciona o listener
        auth.addAuthStateListener(listener)

        // Quando o Flow for cancelado (ex: ViewModel destruído), remove o listener
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }
}