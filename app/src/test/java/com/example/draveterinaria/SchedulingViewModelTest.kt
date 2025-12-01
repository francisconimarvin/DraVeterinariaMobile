package com.example.draveterinaria.viewModels

import com.example.draveterinaria.data.model.*
import com.example.draveterinaria.data.repository.SchedulingRepository
import com.example.draveterinaria.utils.EmailValidator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

@OptIn(ExperimentalCoroutinesApi::class)
class SchedulingViewModelTest {

    // Regla para ejecutar tareas de arquitectura de forma instantánea
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Dispatcher de prueba para Coroutines
    private val testDispatcher = StandardTestDispatcher()

    // Mock del Repositorio
    private val mockRepository = mockk<SchedulingRepository>()


    private val mockEmailValidator = mockk<EmailValidator>()

    private lateinit var viewModel: SchedulingViewModel

    // Datos de ejemplo para las pruebas
    private val testEspecies = listOf(EspecieResponse(1L, "Perro"), EspecieResponse(2L, "Gato"))
    private val testTipos = listOf(TipoServicioResponse(10L, "Consulta"))

    @Before
    fun setup() {
        // 1. Establecer el Dispatcher de prueba como Main Dispatcher
        Dispatchers.setMain(testDispatcher)

        // 2. Definir el comportamiento por defecto de los Mocks
        // Mocks de repositorio:
        coEvery { mockRepository.fetchEspecies() } returns testEspecies
        coEvery { mockRepository.fetchTiposServicio() } returns testTipos


        coEvery { mockEmailValidator.isValid("test@ejemplo.com") } returns true
        coEvery { mockEmailValidator.isValid("email_invalido") } returns false


        // 3. Crear el ViewModel, inyectando ambos Mocks
        viewModel = SchedulingViewModel(
            repository = mockRepository,
            emailValidator = mockEmailValidator
        )

        // 4. Ejecutar las coroutines de inicialización (loadInitialData)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        // Restaurar el Main Dispatcher
        Dispatchers.resetMain()
    }

    // ===================================================================
    // PRUEBAS DE CARGA INICIAL (INIT)
    // ===================================================================

    @Test
    fun init_loadsInitialData_setsEspeciesAndTipos() = runTest {
        // Asumo que el campo de EspecieResponse ahora se llama 'nombre'
        assertEquals(2, viewModel.especies.first().size)
        assertEquals("Perro", viewModel.especies.first().first().nombreEspecie)

        assertEquals(1, viewModel.tiposServicio.first().size)
        assertFalse(viewModel.loading.first())
    }

    // ===================================================================
    // PRUEBAS DE VALIDACIÓN DE MASCOTA
    // (Estas pruebas no cambian, ya que no dependen de la librería de Android)
    // ===================================================================

    @Test
    fun validateMascota_withAllFieldsValid_returnsTrueAndClearsErrors() = runTest {
        val validMascota = MascotaInput(
            especieId = 1L, nombre = "Firulais", raza = "Labrador",
            fechaNacimiento = "2020-01-01", sexo = "Macho", antecedentes = ""
        )
        viewModel.updateMascotaInput(validMascota)
        val result = viewModel.validateMascota()
        assertTrue(result)
        assertTrue(viewModel.validationErrors.first().isEmpty())
    }

    @Test
    fun validateMascota_withMissingRequiredFields_returnsFalseAndShowsErrors() = runTest {
        val invalidMascota = MascotaInput(
            especieId = 0L, nombre = "", raza = "", fechaNacimiento = "2020/01/01",
            sexo = "Macho", antecedentes = ""
        )
        viewModel.updateMascotaInput(invalidMascota)
        val result = viewModel.validateMascota()
        assertFalse(result)
        assertEquals(3, viewModel.validationErrors.first().size)
        assertEquals("El nombre de la mascota es obligatorio.", viewModel.validationErrors.first()["nombre"])
        assertEquals("Debe seleccionar una especie.", viewModel.validationErrors.first()["especieId"])
        assertEquals("La raza es obligatoria.", viewModel.validationErrors.first()["raza"])
    }

    // ===================================================================
    // PRUEBAS DE VALIDACIÓN DE TUTOR (CORREGIDAS Y USANDO MOCK)
    // ===================================================================

    @Test
    fun validateTutor_withValidInput_returnsTrue() = runTest {
        // El Mock garantiza que "test@ejemplo.com" es considerado válido (returns true)

        val validTutor = TutorInput(
            email = "test@ejemplo.com", // Validez controlada por el mock
            rut = "12.345.678-9",
            nombre = "Juan",
            snombre = "", // Añadido
            apaterno = "Perez",
            amaterno = "Lopez",
            telefono = "987654321",
            direccion = "Calle Falsa 123",
        )
        viewModel.updateTutorInput(validTutor)
        val result = viewModel.validateTutor()
        assertTrue(result)
        assertTrue(viewModel.validationErrors.first().isEmpty())
    }

    @Test
    fun validateTutor_withInvalidEmailAndShortRut_returnsFalse() = runTest {
        // El email falla porque el mock devuelve 'false' para "email_invalido"
        val invalidTutor = TutorInput(
            email = "email_invalido", // Falla (Mock devuelve false)
            rut = "12345678-9",       // Falla (le faltan los puntos para la RUT_REGEX estricta)
            nombre = "",              // Falla
            snombre = "",
            apaterno = "",            // Falla
            amaterno = "Lopez",
            telefono = "123",         // Falla (largo < 8)
            direccion = ""            // Falla
        )
        viewModel.updateTutorInput(invalidTutor)
        val result = viewModel.validateTutor()

        assertFalse(result)

        // Verificar el número total de errores (6 errores)
        assertEquals(6, viewModel.validationErrors.first().size)


        assertEquals("Email inválido o incompleto.", viewModel.validationErrors.first()["email"])


        assertEquals("El formato de RUT es inválido (ej: 19.876.543-K).", viewModel.validationErrors.first()["rut"])

        assertEquals("El primer nombre es obligatorio.", viewModel.validationErrors.first()["nombre"])
        assertEquals("El apellido paterno es obligatorio.", viewModel.validationErrors.first()["apaterno"])
        assertEquals("Teléfono inválido.", viewModel.validationErrors.first()["telefono"])
        assertEquals("La dirección es obligatoria.", viewModel.validationErrors.first()["direccion"])
    }
}