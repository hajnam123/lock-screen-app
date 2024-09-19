package com.example.lockscreenapp.ultis

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings
import com.example.lockscreenapp.R

fun Context.openPickWifiSetting() {
    try {
        val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
        if (intent.resolveActivity(this.packageManager) != null) {
            this.startActivity(intent)
        }
    } catch (ex: Exception) {
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }
}

fun Activity?.goToFeedback(email: String? = null) {
    if (this != null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "plain/text"
            putExtra(Intent.EXTRA_EMAIL, email ?: arrayOf("Trustedapp.help@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, buildString {
                append("Feedback")
                append(getString(R.string.app_name))
            })
            putExtra(Intent.EXTRA_TEXT, "Feedback")
        }
        startActivity(Intent.createChooser(intent, "Feedback"))
    }
}
