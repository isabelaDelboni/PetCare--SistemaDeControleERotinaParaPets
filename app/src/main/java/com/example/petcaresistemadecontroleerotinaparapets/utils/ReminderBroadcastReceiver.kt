package com.example.petcaresistemadecontroleerotinaparapets.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.petcaresistemadecontroleerotinaparapets.R 

class ReminderBroadcastReceiver : BroadcastReceiver() {

    // Constantes para os dados passados via Intent
    companion object {
        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_TITLE = "notification_title"
        const val EXTRA_MESSAGE = "notification_message"
        const val CHANNEL_ID = "petcare_reminders" // (Deve ser o mesmo do PetCareApplication)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Lembrete PetCare"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "Você tem um evento agendado."

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Constrói a notificação
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // (Idealmente um ícone de notificação)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Exibe a notificação
        notificationManager.notify(notificationId, notification)
    }
}