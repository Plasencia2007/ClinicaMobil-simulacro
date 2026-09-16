package pe.edu.upeu.clinicamobil.fakes

import pe.edu.upeu.clinicamobil.domain.model.Medico
import pe.edu.upeu.clinicamobil.domain.repository.MedicoRepository

/** Implementación falsa del contrato MedicoRepository: sin delay, capaz de fallar a voluntad. */
class FakeMedicoRepository(
    medicosIniciales: List<Medico> = emptyList(),
    private val fallarAlListar: Boolean = false,
    private val fallarAlRegistrar: Boolean = false
) : MedicoRepository {

    private val medicos = medicosIniciales.toMutableList()
    private var siguienteId = (medicosIniciales.maxOfOrNull { it.id } ?: 0L) + 1

    var registrarLlamadas = 0
        private set

    override suspend fun registrar(medico: Medico): Medico {
        registrarLlamadas++
        if (fallarAlRegistrar) error("Fallo simulado al registrar")
        val conId = medico.copy(id = siguienteId)
        siguienteId += 1
        medicos.add(conId)
        return conId
    }

    override suspend fun listar(): List<Medico> {
        if (fallarAlListar) error("Fallo simulado al listar")
        return medicos.toList()
    }
}
