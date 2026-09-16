package pe.edu.upeu.clinicamobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AtencionTest {

    private val medico = Medico(id = 1L, nombre = "Dr. Pérez", colegiatura = "12345", especialidad = null)

    @Test
    fun rechazaCeroMinutos() {
        assertFailsWith<IllegalArgumentException> {
            Atencion(medico = medico, diagnostico = "Control", duracionMinutos = 0)
        }
    }

    @Test
    fun rechaza121Minutos() {
        assertFailsWith<IllegalArgumentException> {
            Atencion(medico = medico, diagnostico = "Control", duracionMinutos = 121)
        }
    }

    @Test
    fun costoDeTreintaMinutosEs75() {
        val atencion = Atencion(medico = medico, diagnostico = "Control", duracionMinutos = 30)
        assertEquals(75.0, atencion.costo())
    }
}
