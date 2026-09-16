package pe.edu.upeu.clinicamobil.domain.repository

import pe.edu.upeu.clinicamobil.domain.model.Paciente

/**
 * Contrato del padrón de pacientes: quién puede incorporarse al padrón y quién
 * puede consultarlo, sin importar si la fuente de datos es local o remota.
 */
interface PacienteRepository {
    suspend fun registrar(paciente: Paciente): Paciente
    suspend fun listar(): List<Paciente>
}
