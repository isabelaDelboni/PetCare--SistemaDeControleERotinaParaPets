package com.example.petcaresistemadecontroleerotinaparapets.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.petcaresistemadecontroleerotinaparapets.data.local.entities.Evento
import com.example.petcaresistemadecontroleerotinaparapets.receiver.NotificationReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleEventNotification(evento: Evento) {
        try {
            // Converter a String de data (DD/MM/AAAA) para Millisegundos
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.parse(evento.dataEvento)

            if (date == null) return

            // Configura o horário para as 08:00 da manhã do dia do evento
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            // Se a data já passou, não agenda
            if (calendar.timeInMillis <= System.currentTimeMillis()) return

            // 2. Preparar o Intent
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("TITLE", "Lembrete: ${evento.tipoEvento}")
                putExtra("MESSAGE", "Não esqueça: ${evento.observacoes ?: evento.tipoEvento} hoje!")
                putExtra("ID", evento.idEvento)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                evento.idEvento, // ID único para não sobrescrever outros alarmes
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // 3. Agendar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("Scheduler", "Alarme agendado para: ${calendar.time}")

        } catch (e: Exception) {
            Log.e("Scheduler", "Erro ao agendar: ${e.message}")
        }
    }

    fun cancelNotification(evento: Evento) {
        try {
            val intent = Intent(context, NotificationReceiver::class.java)

            // Recria o PendingIntent exato que usamos para agendar (mesmo ID)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                evento.idEvento, // O ID tem que ser o mesmo do agendamento
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Cancela o alarme
            alarmManager.cancel(pendingIntent)
            Log.d("Scheduler", "Alarme cancelado para evento ID: ${evento.idEvento}")

        } catch (e: Exception) {
            Log.e("Scheduler", "Erro ao cancelar alarme: ${e.message}")
        }
    }
}