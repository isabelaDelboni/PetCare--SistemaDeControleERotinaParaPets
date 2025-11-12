package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Retorna o usuário atual ou null se não estiver logado
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Retorna o UID do usuário (necessário para o caminho no Firestore)
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // Login
    suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cadastro
    suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Logout
    fun signOut() {
        auth.signOut()
    }
}