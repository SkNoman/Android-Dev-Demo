package com.example.crud.application

import android.app.Application
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MainApplication: Application() {

    companion object{
        lateinit var instance: MainApplication
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        instance = this
        setDefaultLanguage("en")
    }
    private fun setDefaultLanguage(languageCode: String) {
        val configuration = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}