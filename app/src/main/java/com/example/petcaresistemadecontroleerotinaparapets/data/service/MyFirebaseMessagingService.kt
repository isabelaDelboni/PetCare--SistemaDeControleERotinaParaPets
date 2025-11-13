package com.example.petcaresistemadecontroleerotinaparapets

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Chamado quando chega uma nova notificação e o app está em SEGUNDO PLANO ou FECHADO
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Mensagem recebida de: ${remoteMessage.from}")

        // Se a notificação tiver dados payload, você pode tratar aqui
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM", "Dados da mensagem: ${remoteMessage.data}")
        }

        // Se a notificação tiver corpo de notificação visual
        remoteMessage.notification?.let {
            Log.d("FCM", "Corpo da notificação: ${it.body}")
            mostrarNotificacao(it.title, it.body)
        }
    }

    // Chamado se o token do dispositivo mudar (raro, mas acontece)
    override fun onNewToken(token: String) {
        Log.d("FCM", "Novo token gerado: $token")
        // TODO: Aqui você enviaria esse novo token para o seu servidor/Firestore
        // para saber para quem enviar a notificação depois.
    }

    private fun mostrarNotificacao(title: String?, body: String?) {
        val channelId = "petcare_notificacoes"
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // PendingIntent é necessário para abrir o app ao clicar na notificação
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, channelId)
            //.setSmallIcon(R.drawable.ic_notification) // Você precisará criar um ícone depois
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Ícone genérico temporário
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // A partir do Android Oreo (API 26), é obrigatório criar um canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Lembretes PetCare",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())
    }
}