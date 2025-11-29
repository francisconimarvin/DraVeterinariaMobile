package com.example.draveterinaria.utils

class AndroidEmailValidator : EmailValidator {
    override fun isValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}