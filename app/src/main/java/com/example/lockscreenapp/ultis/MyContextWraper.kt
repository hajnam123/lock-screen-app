package com.example.lockscreenapp.ultis

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.*

class MyContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, language: String): ContextWrapper {
            var ctx: Context = context
            val config: Configuration = ctx.resources.configuration
            val sysLocale: Locale?
            sysLocale = getSystemLocale(config)
            if (language != "" && sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                setSystemLocale(config, locale)
            }
            ctx = ctx.createConfigurationContext(config)
            return MyContextWrapper(ctx)
        }

        private fun getSystemLocale(config: Configuration): Locale {
            return config.locales.get(0)
        }

        private fun setSystemLocale(config: Configuration, locale: Locale?) {
            config.setLocale(locale)
        }
    }
}
