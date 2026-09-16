package pe.edu.upeu.clinicamobil.domain.model

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PacienteTest {

    @Test
    fun rechazaNombreVacio() {
        assertFailsWith<IllegalArgumentException> {
            Paciente(id = 1L, nombre = "   ", dni = "12345678", edad = 30, peso = 70.0)
        }
    }

    @Test
    fun rechazaEdadFueraDeRango() {
        assertFailsWith<IllegalArgumentException> {
            Paciente(id = 1L, nombre = "Ana", dni = "12345678", edad = 121, peso = 70.0)
        }
    }

    @Test
    fun esPediatricoCiertoConDiecisiete() {
        val paciente = Paciente(id = 1L, nombre = "Ana", dni = "12345678", edad = 17, peso = 40.0)
        assertTrue(paciente.esPediatrico)
    }

    @Test
    fun esPediatricoFalsoConDieciocho() {
        val paciente = Paciente(id = 1L, nombre = "Ana", dni = "12345678", edad = 18, peso = 60.0)
        assertFalse(paciente.esPediatrico)
    }
}
