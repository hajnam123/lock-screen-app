package com.example.lockscreenapp.ultis

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.widget.Toast

fun Activity?.turnOffFlash() {
    if (this == null) return
    try {
        val hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (hasFlash) {
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
        }
    } catch (ex: Exception) {
        Toast.makeText(this, "Device do not has Flash", Toast.LENGTH_SHORT).show()
    }
}

fun Activity?.turnOnFlashLight() {
    if (this == null) return
    try {
        val hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (hasFlash) {
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
        }
    } catch (ex: Exception) {
        Toast.makeText(this, "no flash", Toast.LENGTH_SHORT).show()
    }
}