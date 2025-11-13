package com.example.petcaresistemadecontroleerotinaparapets.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
// ✅ IMPORT ADICIONADO
import com.example.petcaresistemadecontroleerotinaparapets.utils.DateConverter

class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(evento: Evento) {
        val triggerAtMillis = DateConverter.parseDateToTimestamp(evento.dataEvento) ?: return

        if (triggerAtMillis < System.currentTimeMillis()) {
            return
        }

        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            // ✅ CORREÇÃO: 'idEvento'
            putExtra(ReminderBroadcastReceiver.EXTRA_NOTIFICATION_ID, evento.idEvento)
            putExtra(ReminderBroadcastReceiver.EXTRA_TITLE, "Lembrete: ${evento.tipoEvento}")
            putExtra(ReminderBroadcastReceiver.EXTRA_MESSAGE, "Evento agendado: ${evento.observacoes ?: evento.tipoEvento}")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            // ✅ CORREÇÃO: 'idEvento'
            evento.idEvento, // Usa o ID do evento como ID do PendingIntent
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
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
            // ✅ CORREÇÃO: 'idEvento'
            evento.idEvento,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}