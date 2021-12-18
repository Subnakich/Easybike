package ru.subnak.easybike.presentation.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EasyBikeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}