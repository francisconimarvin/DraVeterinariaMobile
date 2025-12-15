package com.example.draveterinaria
import com.example.draveterinaria.ui.notifications.NotificationHelper
import android.app.Application

class DraVeterinariaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}