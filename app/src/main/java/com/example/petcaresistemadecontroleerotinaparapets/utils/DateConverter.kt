package com.example.petcaresistemadecontroleerotinaparapets.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Objeto utilitário para converter a data (String) do evento
 * num timestamp (Long) para o AlarmManager.
 */
object DateConverter {

    /**
     * Converte uma string de data (ex: "25/12/2025") em um timestamp (Long).
     * Define um horário padrão (ex: 8:00 da manhã) para o lembrete.
     */
    fun parseDateToTimestamp(dateString: String, hourOfDay: Int = 8): Long? {
        return try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = format.parse(dateString) ?: return null

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            calendar.timeInMillis
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}