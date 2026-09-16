package pe.edu.upeu.clinicamobil.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.clinicamobil.fakes.FakeMedicoRepository

class RegistrarMedicoUseCaseTest {

    @Test
    fun colegiaturaDeCuatroDigitosEsInvalida() = runTest {
        val resultado = RegistrarMedicoUseCase(FakeMedicoRepository())("Dr. Pérez", "1234", "")
        val errores = (resultado.exceptionOrNull() as MedicoInvalidoException).errores
        assertEquals("La colegiatura debe tener entre 5 y 6 dígitos", errores.colegiatura)
    }

    @Test
    fun especialidadDeDosCaracteresEsInvalida() = runTest {
        val resultado = RegistrarMedicoUseCase(FakeMedicoRepository())("Dr. Pérez", "12345", "Ca")
        val errores = (resultado.exceptionOrNull() as MedicoInvalidoException).errores
        assertEquals("La especialidad debe tener al menos 3 caracteres", errores.especialidad)
    }

    @Test
    fun especialidadEnBlancoSeGuardaComoNull() = runTest {
        val medico = RegistrarMedicoUseCase(FakeMedicoRepository())("Dr. Pérez", "12345", "   ").getOrThrow()
        assertNull(medico.especialidad)
    }
}
