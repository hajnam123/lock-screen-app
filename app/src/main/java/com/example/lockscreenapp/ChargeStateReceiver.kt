package com.example.lockscreenapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ChargeStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> {
                println("ACTION_SCREEN_ON")
                val intent1 = Intent(context, FullscreenActivity::class.java)
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent1)
            }
        }
    }
}
