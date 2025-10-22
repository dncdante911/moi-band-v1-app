package com.moi.band

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}