package pe.edu.upeu.clinicamobil.data.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.clinicamobil.domain.model.Paciente

class PacienteRepositorioEnMemoriaTest {

    @Test
    fun idsCorrelativos() = runTest {
        val repository = PacienteRepositorioEnMemoria()

        val primero = repository.registrar(
            Paciente(id = 0L, nombre = "Ana", dni = "12345678", edad = 30, peso = 60.0)
        )
        val segundo = repository.registrar(
            Paciente(id = 0L, nombre = "Luis", dni = "87654321", edad = 40, peso = 75.0)
        )

        assertEquals(1L, primero.id)
        assertEquals(2L, segundo.id)
    }

    @Test
    fun listarEnOrdenDeRegistro() = runTest {
        val repository = PacienteRepositorioEnMemoria()
        repository.registrar(Paciente(id = 0L, nombre = "Ana", dni = "12345678", edad = 30, peso = 60.0))
        repository.registrar(Paciente(id = 0L, nombre = "Luis", dni = "87654321", edad = 40, peso = 75.0))

        val lista = repository.listar()

        assertEquals(listOf("Ana", "Luis"), lista.map { it.nombre })
    }
}
