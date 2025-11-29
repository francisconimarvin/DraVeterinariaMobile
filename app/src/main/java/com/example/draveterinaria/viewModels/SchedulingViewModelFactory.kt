package com.example.draveterinaria.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.draveterinaria.data.repository.SchedulingRepository
import com.example.draveterinaria.utils.AndroidEmailValidator
import com.example.draveterinaria.utils.EmailValidator
import java.lang.IllegalArgumentException

class SchedulingViewModelFactory(
    private val repository: SchedulingRepository,
    private val emailValidator: EmailValidator
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchedulingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SchedulingViewModel(repository, emailValidator) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}