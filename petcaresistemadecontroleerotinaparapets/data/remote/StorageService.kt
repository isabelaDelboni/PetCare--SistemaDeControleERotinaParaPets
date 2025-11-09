package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageService {
    // Referência para a raiz do Storage
    private val storageRef = FirebaseStorage.getInstance().reference

    // Função para fazer upload da foto do Pet
    // Retorna a URL pública da imagem (downloadUrl)
    suspend fun uploadPetPhoto(imageUri: Uri, userId: String): Result<String> {
        return try {
            // Cria um nome único para a imagem (ex: users/uid/pets/uuid.jpg)
            val filename = UUID.randomUUID().toString() + ".jpg"
            val photoRef = storageRef.child("users/$userId/pets/$filename")

            // 1. Faz o upload do arquivo
            photoRef.putFile(imageUri).await()
            Log.d("StorageService", "Upload concluído: $filename")

            // 2. Pega a URL pública para download
            val downloadUrl = photoRef.downloadUrl.await()
            Log.d("StorageService", "URL da foto: $downloadUrl")

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Log.e("StorageService", "Erro no upload", e)
            Result.failure(e)
        }
    }
}