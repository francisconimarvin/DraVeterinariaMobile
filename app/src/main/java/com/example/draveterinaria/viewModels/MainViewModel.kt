package com.example.draveterinaria.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.draveterinaria.data.local.SecureStorage
import com.example.draveterinaria.data.model.LoginRequest
import com.example.draveterinaria.data.remote.RetrofitClient
import com.example.draveterinaria.navigation.NavigationEvent
import com.example.draveterinaria.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.draveterinaria.utils.parseErrorMessage

// ----------------------
// Estados de Login
// ----------------------
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String, val role: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

// ----------------------
// MainViewModel
// ----------------------
class MainViewModel(

    application: Application
) : AndroidViewModel(application) {

    // --- Storage seguro ---
    private val secureStorage = SecureStorage(application)

    // --- Navegación ---
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // --- Estado de Login ---
    private val _loginStatus = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginStatus: StateFlow<LoginState> = _loginStatus.asStateFlow()

    // ----------------------
    // LOGIN
    // ----------------------
    fun performLogin(email: String, password: String) {

        if (_loginStatus.value == LoginState.Loading) return

        _loginStatus.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.loginApiService.login(request)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        val jwtToken = body.token
                        val role = body.role

                        //  Guardar sesión local
                        secureStorage.saveToken(jwtToken)

                        _loginStatus.value = LoginState.Success(jwtToken, role)

                    } else {
                        _loginStatus.value =
                            LoginState.Error("Respuesta vacía del servidor.")
                    }
                } else {

                    val errorMsg =
                        response.errorBody()?.string() ?: "Credenciales inválidas."
                    _loginStatus.value = LoginState.Error(errorMsg)

                    val errorBody = response.errorBody()?.string()
                    val message = parseErrorMessage(errorBody)
                    _loginStatus.value = LoginState.Error(message)

                }
            } catch (e: Exception) {
                _loginStatus.value = LoginState.Error(
                    "Error de conexión: ${e.message ?: "Servidor no disponible"}"
                )
            }
        }
    }

    fun resetLoginStatus() {
        _loginStatus.value = LoginState.Idle
    }

    // ----------------------
    // SESIÓN
    // ----------------------
    fun hasSession(): Boolean {
        return secureStorage.getToken() != null
    }

    fun logout() {
        secureStorage.clear()
    }

    // ----------------------
    // NAVEGACIÓN
    // ----------------------
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
