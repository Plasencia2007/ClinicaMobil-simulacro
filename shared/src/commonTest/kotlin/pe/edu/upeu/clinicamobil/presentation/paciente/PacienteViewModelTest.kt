package pe.edu.upeu.clinicamobil.presentation.paciente

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pe.edu.upeu.clinicamobil.domain.model.Paciente
import pe.edu.upeu.clinicamobil.domain.usecase.ListarPacientesUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.RegistrarPacienteUseCase
import pe.edu.upeu.clinicamobil.fakes.FakePacienteRepository

@OptIn(ExperimentalCoroutinesApi::class)
class PacienteViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel(repository: FakePacienteRepository): PacienteViewModel =
        PacienteViewModel(RegistrarPacienteUseCase(repository), ListarPacientesUseCase(repository))

    @Test
    fun arrancaEnSinPacientes() = runTest {
        val viewModel = crearViewModel(FakePacienteRepository())

        assertEquals(PacienteUiState.FaseListado.SinPacientes, viewModel.uiState.value.fase)
    }

    @Test
    fun muestraLaLineaSecundariaDelAnexoB() = runTest {
        val paciente = Paciente(id = 1L, nombre = "Ana", dni = "12345678", edad = 34, peso = 70.5)
        val viewModel = crearViewModel(FakePacienteRepository(pacientesIniciales = listOf(paciente)))

        val fase = viewModel.uiState.value.fase
        assertTrue(fase is PacienteUiState.FaseListado.ConPacientes)
        assertEquals("34 años · 70.5 kg", fase.pacientes.first().lineaSecundaria)
    }

    @Test
    fun pasaAErrorSiElRepositorioFalla() = runTest {
        val viewModel = crearViewModel(FakePacienteRepository(fallarAlListar = true))

        assertTrue(viewModel.uiState.value.fase is PacienteUiState.FaseListado.Error)
    }

    @Test
    fun erroresDeValidacionCaenEnElFormularioNoEnLaFase() = runTest {
        val viewModel = crearViewModel(FakePacienteRepository())

        viewModel.registrar()

        assertEquals(PacienteUiState.FaseListado.SinPacientes, viewModel.uiState.value.fase)
        assertEquals("El nombre es obligatorio", viewModel.uiState.value.formulario.errorNombre)
    }

    @Test
    fun registrarLimpiaElFormularioYRecarga() = runTest {
        val viewModel = crearViewModel(FakePacienteRepository())

        viewModel.onNombreChange("Ana Torres")
        viewModel.onDniChange("12345678")
        viewModel.onEdadChange("30")
        viewModel.onPesoChange("65.5")
        viewModel.registrar()

        assertEquals("", viewModel.uiState.value.formulario.nombre)
        assertTrue(viewModel.uiState.value.fase is PacienteUiState.FaseListado.ConPacientes)
    }
}
