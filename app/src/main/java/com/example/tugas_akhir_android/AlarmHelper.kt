package com.example.tugas_akhir_android

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmHelper {
    fun scheduleNotification(context: Context, timeInMillis: Long, jadwalId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("jadwalId", jadwalId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            jadwalId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }
}