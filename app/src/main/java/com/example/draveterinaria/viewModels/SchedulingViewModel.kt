package com.example.draveterinaria.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.draveterinaria.data.model.* // Modelos Input y Response
import com.example.draveterinaria.data.repository.SchedulingRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.draveterinaria.utils.EmailValidator


private val RUT_REGEX = Regex("^\\d{1,9}-[0-9Kk]$")
class SchedulingViewModel(
    private val repository: SchedulingRepository = SchedulingRepository(),
    private val emailValidator: EmailValidator

) : ViewModel() {

    // ------------------- ESTADO DE LA UI Y FORMULARIO -------------------

    // Estado del Formulario
    private val _tutorInput = MutableStateFlow(TutorInput("", "", "", "", "", "", "", ""))
    val tutorInput = _tutorInput.asStateFlow()

    private val _mascotaInput = MutableStateFlow(MascotaInput(0L, "", "", "", "", ""))
    val mascotaInput = _mascotaInput.asStateFlow()

    private val _servicioInput = MutableStateFlow(ServicioInput(0L, 0L, 0, ""))
    val servicioInput = _servicioInput.asStateFlow()

    // Estado para Listas Dropdown (Similar a useState de React)
    private val _especies = MutableStateFlow<List<EspecieResponse>>(emptyList())
    val especies = _especies.asStateFlow()

    private val _tiposServicio = MutableStateFlow<List<TipoServicioResponse>>(emptyList())
    val tiposServicio = _tiposServicio.asStateFlow()

    private val _subtiposServicio = MutableStateFlow<List<SubtipoServicioResponse>>(emptyList())
    val subtiposServicio = _subtiposServicio.asStateFlow()

    // Estado del Proceso (Para la UI)
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _registrationStatus = MutableSharedFlow<String>() // Mensaje de éxito/error
    val registrationStatus = _registrationStatus.asSharedFlow()

    init {
        loadInitialData()
        observeTipoServicioChange()
    }

    // ------------------- CARGA DE DATOS INICIALES (useEffect en React) -------------------

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _loading.value = true
                // Carga Especies y Tipos de forma concurrente
                _especies.value = repository.fetchEspecies()
                _tiposServicio.value = repository.fetchTiposServicio()
            } catch (e: Exception) {
                _registrationStatus.emit("Error al cargar datos iniciales: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    // ------------------- MANEJO DE ERRORES -----------------------------------------

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors = _validationErrors.asStateFlow()

    // 2. Función para limpiar errores (útil al cambiar de paso)
    fun clearErrors() {
        _validationErrors.value = emptyMap()
    }

    // 3. Validación del Paso 1: Mascota
    fun validateMascota(): Boolean {
        val errors = mutableMapOf<String, String>()
        val input = _mascotaInput.value

        if (input.nombre.isBlank()) errors["nombre"] = "El nombre de la mascota es obligatorio."
        if (input.especieId <= 0) errors["especieId"] = "Debe seleccionar una especie."
        if (input.raza.isBlank()) errors["raza"] = "La raza es obligatoria."
        if (input.fechaNacimiento.isBlank()) errors["fechaNacimiento"] = "La fecha de nacimiento es obligatoria."
        if (input.sexo.isBlank()) errors["sexo"] = "El sexo es obligatorio."

        // Si la validación falla, se actualiza el StateFlow
        _validationErrors.value = errors
        return errors.isEmpty()
    }

    // 4. Validación del Paso 2: Tutor
    fun validateTutor(): Boolean {
        val errors = mutableMapOf<String, String>()
        val input = _tutorInput.value


        if (input.email.isBlank() || !emailValidator.isValid(input.email))errors["email"] = "Email inválido o incompleto."
        if (input.rut.isBlank()) {
            errors["rut"] = "El RUT es obligatorio."
        } else if (!input.rut.matches(RUT_REGEX)) {
            errors["rut"] = "El formato de RUT es inválido (ej: 19.876.543-K)."
        }
        if (input.nombre.isBlank()) errors["nombre"] = "El primer nombre es obligatorio."
        if (input.apaterno.isBlank()) errors["apaterno"] = "El apellido paterno es obligatorio."
        if (input.telefono.isBlank() || input.telefono.length < 8) errors["telefono"] = "Teléfono inválido."
        if (input.direccion.isBlank()) errors["direccion"] = "La dirección es obligatoria."

        _validationErrors.value = errors
        return errors.isEmpty()
    }
    // ------------------- MANEJO DE CAMBIOS  -------------------

    // Ejemplo de Mascota
    fun onMascotaNombreChange(value: String) {
        _mascotaInput.update { it.copy(nombre = value) }
    }

    fun onMascotaEspecieChange(id: Long) {
        _mascotaInput.update { it.copy(especieId = id) }
    }

    // Ejemplo de Tutor
    fun onTutorRutChange(value: String) {
        _tutorInput.update { it.copy(rut = value) }
    }

    // Ejemplo de Servicio
    fun onServicioFechaChange(value: String) {
        _servicioInput.update { it.copy(fecha = value) }
    }

    fun updateMascotaInput(input: MascotaInput) {
        _mascotaInput.value = input
    }

    // Función para actualizar el estado COMPLETO del TutorInput
    fun updateTutorInput(input: TutorInput) {
        _tutorInput.value = input
    }

    // ------------------- LÓGICA CONDICIONAL (useEffect dependiente de servicio.tipo) -------------------

    private fun observeTipoServicioChange() {
        // Ejecuta la carga de subtipos cada vez que cambia el tipo de servicio seleccionado
        viewModelScope.launch {
            _servicioInput.map { it.tipoId }
                .distinctUntilChanged()
                .collect { tipoId ->
                    if (tipoId > 0) {
                        try {
                            _loading.value = true
                            _subtiposServicio.value = repository.fetchSubtipos(tipoId)
                        } catch (e: Exception) {
                            _registrationStatus.emit("Error al cargar subtipos: ${e.message}")
                            _subtiposServicio.value = emptyList()
                        } finally {
                            _loading.value = false
                        }
                    } else {
                        _subtiposServicio.value = emptyList()
                    }
                }
        }
    }

    fun onServicioTipoChange(id: Long) {
        // Al cambiar el tipo, reseteamos el subtipo y el precio
        _servicioInput.update {
            it.copy(tipoId = id, subtipoId = 0L, precio = 0)
        }
    }

    fun onServicioSubtipoChange(id: Long) {
        // Encontramos el precio y lo actualizamos
        val selectedSubtipo = _subtiposServicio.value.find { it.idSubtipo == id }
        val precio = selectedSubtipo?.precio ?: 0

        _servicioInput.update {
            it.copy(subtipoId = id, precio = precio)
        }
    }

    // ------------------- TRANSACCIÓN FINAL (submitServicio en React) -------------------

    fun submitRegistration() {


        viewModelScope.launch {
            _loading.value = true
            try {
                val resultMessage = repository.completeScheduling(
                    tutor = _tutorInput.value,
                    mascota = _mascotaInput.value,
                    servicio = _servicioInput.value
                )
                _registrationStatus.emit(resultMessage) // Emite "Registro completado con éxito"
                resetForm() // Reinicia los estados después del éxito
            } catch (e: Exception) {
                // Manejo de errores de red o del servidor
                _registrationStatus.emit("Fallo en el registro: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    private fun resetForm() {
        // Función para limpiar los estados después del registro exitoso
        _tutorInput.value = TutorInput("", "", "", "", "", "", "", "")
        _mascotaInput.value = MascotaInput(0L, "", "", "", "", "")
        _servicioInput.value = ServicioInput(0L, 0L, 0, "")
        _subtiposServicio.value = emptyList()
    }
}