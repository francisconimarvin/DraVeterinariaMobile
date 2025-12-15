package com.example.draveterinaria.security

import android.app.KeyguardManager
import android.content.Context
import androidx.biometric.BiometricManager

object DeviceSecurityUtils {

    fun hasDeviceCredential(context: Context): Boolean {
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return km.isDeviceSecure
    }

    fun canUseBiometrics(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }
}
