package com.example.tugas_akhir_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "Jadwal Makan"
        val message = inputData.getString("message") ?: "Saatnya makan!"
        val notificationId = inputData.getInt("notificationId", 0)

        sendNotification(title, message, notificationId)
        return Result.success()
    }

    private fun sendNotification(title: String, message: String, notificationId: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel if Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "jadwal_makan_channel",
                "Jadwal Makan Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Add sound to the notification
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL)

        val notification = NotificationCompat.Builder(applicationContext, "jadwal_makan_channel")
            .setSmallIcon(R.drawable.ic_notifications_24) // Ganti dengan ikon notifikasi Anda
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(notificationSound)  // Add sound here
            .build()

        notificationManager.notify(notificationId, notification)
    }
}