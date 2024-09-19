package com.example.lockscreenapp.ultis

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File
import java.util.Properties


fun ImageView.loadImage(uri: Uri?) {
    if (uri == null) return
    Glide.with(this).load(uri).into(this)
}

fun ImageView.loadImage(string: String?) {
    Glide.with(this).load(string).into(this)
}

fun ImageView.loadThumbnails(string: String?) {
    Glide.with(this).load(string).sizeMultiplier(0.5f).into(this)
}

fun ImageView.loadImage(file: File) {
    Glide.with(this).load(file).into(this)
}

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

fun String.deleteFileIfExist() {
    try {
        val file = File(this)
        if (file.exists()) file.delete()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.safeGetValueFromConfig(key: String): String {
    return try {
        val properties = Properties()
        val inputStream = assets.open("config.properties")
        properties.load(inputStream)
        properties.getProperty(key) ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
