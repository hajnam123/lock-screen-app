package com.example.lockscreenapp.ultis

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.LocaleList
import com.example.lockscreenapp.R
import java.util.Locale

class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {

        fun getLocalesListFirst(resources: Resources): ArrayList<Language> {
            val codes = resources.getStringArray(R.array.supported_locales_first)
            val names = resources.getStringArray(R.array.supported_locales_names_first)
            val image = arrayListOf<Int>()

            image.add(R.mipmap.ic_uk)
            image.add(R.mipmap.ic_spain)
            image.add(R.mipmap.ic_vn)
            image.add(R.mipmap.ic_it)
            image.add(R.mipmap.ic_hindi)
            image.add(R.mipmap.ic_china)
            image.add(R.mipmap.ic_german)
            image.add(R.mipmap.ic_portugal)
            image.add(R.mipmap.ic_france)
            image.add(R.mipmap.ic_indonesia)
            image.add(R.mipmap.ic_thailand)
            image.add(R.mipmap.ic_korean)
            image.add(R.mipmap.ic_japanese)
            image.add(R.mipmap.ic_malaysia)
            image.add(R.mipmap.ic_russian)
            image.add(R.mipmap.ic_arabic)
            image.add(R.mipmap.ic_taiwan)
            image.add(R.mipmap.ic_myanmar)
            image.add(R.mipmap.ic_turkrish)
            image.add(R.mipmap.ic_pakistan)

            val localeList = arrayListOf<Language>()

            codes.forEachIndexed { index, _ ->
                localeList.add(Language(index, names[index], codes[index], image[index]))
            }

            return localeList
        }

        fun getLocalesList(resources: Resources): ArrayList<Language> {
            val codes = resources.getStringArray(R.array.supported_locales_first)
            val names = resources.getStringArray(R.array.supported_locales_names_first)
            val image = arrayListOf<Int>()

            image.add(R.mipmap.ic_uk)
            image.add(R.mipmap.ic_german)
            image.add(R.mipmap.ic_portu)
            image.add(R.mipmap.ic_spain)
            image.add(R.mipmap.ic_hindi)
            image.add(R.mipmap.ic_it)
            image.add(R.mipmap.ic_portugal)
            image.add(R.mipmap.ic_france)
            image.add(R.mipmap.ic_indonesia)
            image.add(R.mipmap.ic_thailand)
            image.add(R.mipmap.ic_korean)
            image.add(R.mipmap.ic_japanese)
            image.add(R.mipmap.ic_malaysia)
            image.add(R.mipmap.ic_russian)
            image.add(R.mipmap.ic_arabic)
            image.add(R.mipmap.ic_taiwan)
            image.add(R.mipmap.ic_myanmar)
            image.add(R.mipmap.ic_turkrish)
            image.add(R.mipmap.ic_pakistan)

            val localeList = arrayListOf<Language>()

            codes.forEachIndexed { index, _ ->
                localeList.add(Language(index, names[index], codes[index], image[index]))
            }

            return localeList
        }

        fun updateLocale(ctx: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = ctx
            val resources = context.resources
            val configuration = resources.configuration
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            context = context.createConfigurationContext(configuration)
            return ContextUtils(context)
        }
    }
}

data class Language(
    val id: Int, val name: String, val code: String, val image: Int
)

