package com.example.draveterinaria.utils

interface EmailValidator {
    fun isValid(email: String): Boolean
}