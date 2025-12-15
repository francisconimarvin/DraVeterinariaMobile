package com.example.draveterinaria.viewModels

import android.app.Application
import com.example.draveterinaria.data.local.SecureStorage
import com.example.draveterinaria.data.model.LoginRequest
import com.example.draveterinaria.data.model.LoginResponse
import com.example.draveterinaria.data.remote.LoginApiService
import com.example.draveterinaria.data.remote.RetrofitClient
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockApplication = mockk<Application>(relaxed = true)
    private val mockStorage = mockk<SecureStorage>(relaxed = true)
    private val mockApiService = mockk<LoginApiService>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(RetrofitClient)
        every { RetrofitClient.loginApiService } returns mockApiService

        viewModel = MainViewModel(mockStorage, mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `performLogin success updates loginStatus to Success`() = runTest {
        val email = "test@example.com"
        val password = "123456"
        val token = "fake-jwt"
        val role = "user"

        val response = Response.success(LoginResponse(token, role))
        coEvery { mockApiService.login(LoginRequest(email, password)) } returns response
        coEvery { mockStorage.saveToken(token) } just Runs

        viewModel.performLogin(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        val status = viewModel.loginStatus.first()
        assert(status is LoginState.Success)
        assertEquals(token, (status as LoginState.Success).token)
        assertEquals(role, status.role)
    }

    @Test
    fun `performLogin failure updates loginStatus to Error`() = runTest {
        val email = "wrong@example.com"
        val password = "wrongpass"

        val errorBody = "Credenciales inválidas".toResponseBody("text/plain".toMediaType())
        val response = Response.error<LoginResponse>(400, errorBody)
        coEvery { mockApiService.login(LoginRequest(email, password)) } returns response

        viewModel.performLogin(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        val status = viewModel.loginStatus.first()
        assert(status is LoginState.Error)
        assertEquals("Credenciales inválidas", (status as LoginState.Error).message)
    }

    @Test
    fun `performLogin exception updates loginStatus to Error`() = runTest {
        val email = "test@example.com"
        val password = "123456"

        coEvery { mockApiService.login(LoginRequest(email, password)) } throws Exception("Server down")

        viewModel.performLogin(email, password)
        testDispatcher.scheduler.advanceUntilIdle()

        val status = viewModel.loginStatus.first()
        assert(status is LoginState.Error)
        assert((status as LoginState.Error).message.contains("Error de conexión"))
    }
}
