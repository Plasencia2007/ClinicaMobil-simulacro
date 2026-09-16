package pe.edu.upeu.clinicamobil.fakes

import pe.edu.upeu.clinicamobil.domain.model.Paciente
import pe.edu.upeu.clinicamobil.domain.repository.PacienteRepository

/** Implementación falsa del contrato PacienteRepository: sin delay, capaz de fallar a voluntad. */
class FakePacienteRepository(
    pacientesIniciales: List<Paciente> = emptyList(),
    private val fallarAlListar: Boolean = false,
    private val fallarAlRegistrar: Boolean = false
) : PacienteRepository {

    private val pacientes = pacientesIniciales.toMutableList()
    private var siguienteId = (pacientesIniciales.maxOfOrNull { it.id } ?: 0L) + 1

    var registrarLlamadas = 0
        private set

    override suspend fun registrar(paciente: Paciente): Paciente {
        registrarLlamadas++
        if (fallarAlRegistrar) error("Fallo simulado al registrar")
        val conId = paciente.copy(id = siguienteId)
        siguienteId += 1
        pacientes.add(conId)
        return conId
    }

    override suspend fun listar(): List<Paciente> {
        if (fallarAlListar) error("Fallo simulado al listar")
        return pacientes.toList()
    }
}
