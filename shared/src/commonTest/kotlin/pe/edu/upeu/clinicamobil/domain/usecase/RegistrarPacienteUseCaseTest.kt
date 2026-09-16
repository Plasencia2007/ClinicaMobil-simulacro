package pe.edu.upeu.clinicamobil.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.clinicamobil.fakes.FakePacienteRepository

class RegistrarPacienteUseCaseTest {

    @Test
    fun aceptaUnPacienteValido() = runTest {
        val useCase = RegistrarPacienteUseCase(FakePacienteRepository())

        val resultado = useCase(nombre = "Ana Torres", dni = "12345678", edad = "30", peso = "65.5")

        assertTrue(resultado.isSuccess)
    }

    @Test
    fun elIdLoAsignaElRepositorio() = runTest {
        val useCase = RegistrarPacienteUseCase(FakePacienteRepository())

        val paciente = useCase(nombre = "Ana Torres", dni = "12345678", edad = "30", peso = "65.5").getOrThrow()

        assertEquals(1L, paciente.id)
    }

    @Test
    fun elFalloDelRepositorioLlegaComoResultFailure() = runTest {
        val useCase = RegistrarPacienteUseCase(FakePacienteRepository(fallarAlRegistrar = true))

        val resultado = useCase(nombre = "Ana Torres", dni = "12345678", edad = "30", peso = "65.5")

        assertTrue(resultado.isFailure)
    }

    @Test
    fun mensajeNombreVacio() = runTest {
        val resultado = RegistrarPacienteUseCase(FakePacienteRepository())("", "12345678", "30", "65.5")
        val errores = (resultado.exceptionOrNull() as PacienteInvalidoException).errores
        assertEquals("El nombre es obligatorio", errores.nombre)
    }

    @Test
    fun mensajeDniObligatorio() = runTest {
        val resultado = RegistrarPacienteUseCase(FakePacienteRepository())("Ana", "", "30", "65.5")
        val errores = (resultado.exceptionOrNull() as PacienteInvalidoException).errores
        assertEquals("El DNI es obligatorio", errores.dni)
    }

    @Test
    fun mensajeDniConFormatoInvalido() = runTest {
        val resultado = RegistrarPacienteUseCase(FakePacienteRepository())("Ana", "123", "30", "65.5")
        val errores = (resultado.exceptionOrNull() as PacienteInvalidoException).errores
        assertEquals("El DNI debe tener 8 dígitos", errores.dni)
    }

    @Test
    fun mensajeEdadFueraDeRango() = runTest {
        val resultado = RegistrarPacienteUseCase(FakePacienteRepository())("Ana", "12345678", "200", "65.5")
        val errores = (resultado.exceptionOrNull() as PacienteInvalidoException).errores
        assertEquals("La edad debe estar entre 0 y 120", errores.edad)
    }

    @Test
    fun mensajePesoMenorOIgualACero() = runTest {
        val resultado = RegistrarPacienteUseCase(FakePacienteRepository())("Ana", "12345678", "30", "0")
        val errores = (resultado.exceptionOrNull() as PacienteInvalidoException).errores
        assertEquals("El peso debe ser mayor a 0", errores.peso)
    }
}
