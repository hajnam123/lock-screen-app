@file:Suppress("unused")

package com.example.lockscreenapp.ultis

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.ads.bkplus_ads.core.callforward.BkPlusAppOpenAdManager
import com.bkplus.android.ultis.observeOnce


fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.toBitmap(): Bitmap {
    val returnedBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)
    val bgDrawable = this.background
    if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
    this.draw(canvas)
    return returnedBitmap
}

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(key: String = "result", result: T? = null) {
    findNavController().currentBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.getNavigationResult(key: String = "result", result: (T) -> Unit) {
    val state = findNavController().currentBackStackEntry?.savedStateHandle
    state?.getLiveData<T>(key)?.observeOnce(viewLifecycleOwner) { it ->
        result.invoke(it)
    }
    state?.remove<T>(key)
}


fun NavController.safeNavigateWithArgs(direction: NavDirections, bundle: Bundle? = Bundle()) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction.actionId, bundle)
    }
}

fun Fragment.findNavControllerSafely(): NavController? {
    return if (isAdded) {
        try {
            findNavController()
        } catch (e: Exception) {
            null
        }
    } else {
        null
    }
}

inline fun View.showIf(condition: () -> Boolean): View {
    if (visibility != View.VISIBLE && condition()) {
        visibility = View.VISIBLE
    }
    return this
}

inline fun View.hideIf(condition: () -> Boolean): View {
    if (visibility != View.INVISIBLE && condition()) {
        visibility = View.INVISIBLE
    }
    return this
}

inline fun View.goneIf(condition: () -> Boolean): View {
    if (visibility != View.GONE && condition()) {
        visibility = View.GONE
    }
    return this
}

inline fun View.hiddenIf(condition: () -> Boolean): View {
    visibility = if (condition()) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}


inline fun <reified T : Parcelable?> Bundle.getParcelableCustom(key: String?): T? {
    val entity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        this.getParcelable(key)
    }
    return entity
}

fun Fragment.openWebViewer(url: String) {
    try {
        var webpage = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        activity?.let {
            if (intent.resolveActivity(it.packageManager) != null) {
                BkPlusAppOpenAdManager.disableAdResume()
                startActivity(intent)
            }
        }
    } catch (e: Exception) {
        //User does not have browser
        e.printStackTrace()
    }
}
