package com.example.petcaresistemadecontroleerotinaparapets

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetCareApplication : Application() {

    companion object {
        const val CHANNEL_ID = "petcare_reminders" // (O mesmo ID do Receiver)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    /**
     * Cria o Canal de Notificação (Obrigatório para Android 8+)
     * para o RF04.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lembretes de Eventos PetCare"
            val descriptionText = "Notificações para vacinas, banhos e outros eventos."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Registra o canal no sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}