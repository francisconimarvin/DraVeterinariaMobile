package com.example.draveterinaria.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.draveterinaria.navigation.NavigationEvent
import com.example.draveterinaria.navigation.Screen
import com.example.draveterinaria.data.model.LoginRequest
import com.example.draveterinaria.data.model.LoginResponse
import com.example.draveterinaria.data.remote.RetrofitClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.draveterinaria.utils.parseErrorMessage

// Clase sellada para representar los estados posibles del Login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    // La respuesta final debe contener el token y el rol
    data class Success(val token: String, val role: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class MainViewModel : ViewModel() {

    // --- Navegación ---
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // --- Estado de Login ---
    private val _loginStatus = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginStatus: StateFlow<LoginState> = _loginStatus.asStateFlow()


    /**
     * Intenta iniciar sesión con el email y la contraseña.
     * Gestiona el estado de carga, éxito y error.
     */
    fun performLogin(email: String, password: String) {
        // Evita doble clic o múltiples peticiones
        if (_loginStatus.value == LoginState.Loading) return

        _loginStatus.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email = email, password = password)

                // Llamada al API de Login (puerto 8095, configurado en RetrofitClient)
                val response = RetrofitClient.loginApiService.login(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {

                        val jwtToken = body.token
                        val userRole = body.role

                        // Emite el token y el rol para que la UI lo guarde y navegue
                        _loginStatus.value = LoginState.Success(jwtToken, userRole)
                    } else {
                        _loginStatus.value = LoginState.Error("Respuesta vacía del servidor.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = parseErrorMessage(errorBody)
                    _loginStatus.value = LoginState.Error(message)
                }
            } catch (e: Exception) {
                // Manejo de errores de conexión (servidor caído, timeout, etc.)
                _loginStatus.value = LoginState.Error("Error de conexión: ${e.message ?: "Verifique su IP o servidor"}")
            }
        }
    }

    /** Permite resetear el estado del login a Idle (para reintentar o limpiar el error). */
    fun resetLoginStatus() {
        _loginStatus.value = LoginState.Idle
    }

    // --- Lógica de Navegación ---
    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}

