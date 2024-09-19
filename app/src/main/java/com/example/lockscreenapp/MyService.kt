package com.example.lockscreenapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.IBinder
import android.util.Log


class MyService : Service() {
    companion object {
        var serviceIsRunning: Boolean = false
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        try {
            // From Android 9 Pie if your service does not call startForeground within 5 seconds after it has been started with the command startForegroundService ... then it produces an ANR + Crash
            val channelId = createNotificationChannel("my_service", "My Background Service")
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            val notification = Notification.Builder(this, channelId).setOngoing(true)
                .setContentTitle(getString(R.string.app_name) + " is in foreground")
                .setContentText("App is running").setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build()
            startForeground(1, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val chargeStateReceiver = ChargeStateReceiver()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            intent?.let {
                val filter = IntentFilter()
                filter.addAction(Intent.ACTION_SCREEN_ON)
                this.registerReceiver(chargeStateReceiver, filter)

                val channelId = createNotificationChannel("my_service", "My Background Service")
                val notificationIntent = Intent(this, MainActivity::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(
                    this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
                )
                val notification = Notification.Builder(this, channelId).setOngoing(true)
                    .setContentTitle(getString(R.string.app_name) + " is in foreground")
                    .setContentText("App is running").setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent).build()
                startForeground(1, notification)
            }
        } catch (e: Exception) {
            stopSelf()
            e.printStackTrace()
        }

        return START_REDELIVER_INTENT
    }
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId, channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d(this.javaClass.simpleName, "Task removed")
        serviceIsRunning = true
    }

}