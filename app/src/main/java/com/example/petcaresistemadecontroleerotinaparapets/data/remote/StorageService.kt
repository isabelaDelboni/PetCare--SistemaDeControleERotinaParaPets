package com.example.petcaresistemadecontroleerotinaparapets.data.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject // <--- IMPORTANTE

// ADICIONADO: @Inject constructor()
class StorageService @Inject constructor() {
    private val storageRef = FirebaseStorage.getInstance().reference

    suspend fun uploadPetPhoto(imageUri: Uri, userId: String): Result<String> {
        return try {
            val filename = UUID.randomUUID().toString() + ".jpg"
            val photoRef = storageRef.child("users/$userId/pets/$filename")

            photoRef.putFile(imageUri).await()
            Log.d("StorageService", "Upload concluído: $filename")

            val downloadUrl = photoRef.downloadUrl.await()
            Log.d("StorageService", "URL da foto: $downloadUrl")

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Log.e("StorageService", "Erro no upload", e)
            Result.failure(e)
        }
    }
}