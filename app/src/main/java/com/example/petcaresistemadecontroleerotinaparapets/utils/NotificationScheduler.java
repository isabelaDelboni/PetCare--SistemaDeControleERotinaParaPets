package com.example.petcaresistemadecontroleerotinaparapets.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import java.util.concurrent.TimeUnit

/**
 * Gerencia o agendamento (RF04) de notificações usando AlarmManager.
 */
class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(evento: Evento) {
        // Converte a data string (ex: "20/12/2025") em um timestamp
        val triggerAtMillis = DateConverter.parseDateToTimestamp(evento.dataEvento) ?: return

        // Não agenda eventos que já passaram
        if (triggerAtMillis < System.currentTimeMillis()) {
            return
        }

        // Cria o Intent que será disparado
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderBroadcastReceiver.EXTRA_NOTIFICATION_ID, evento.eventoId)
            putExtra(ReminderBroadcastReceiver.EXTRA_TITLE, "Lembrete: ${evento.tipoEvento}")
            putExtra(ReminderBroadcastReceiver.EXTRA_MESSAGE, "Evento agendado: ${evento.observacoes ?: evento.tipoEvento}")
        }

        val pendingIntent = PendingIntent.getBroadcast(
                context,
                evento.eventoId, // Usa o ID do evento como ID do PendingIntent
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Verifica se o app pode agendar alarmes exatos
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            )
        } else {
            // Fallback para alarme não-exato se a permissão for negada
            alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            )
        }
    }

    fun cancelNotification(evento: Evento) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                evento.eventoId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}