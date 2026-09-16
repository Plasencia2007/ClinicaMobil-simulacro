package pe.edu.upeu.clinicamobil.domain.repository

import pe.edu.upeu.clinicamobil.domain.model.Medico

/**
 * Contrato del cuerpo médico: quién puede incorporarse al cuerpo clínico y
 * quién puede consultarlo, sin importar si la fuente de datos es local o remota.
 */
interface MedicoRepository {
    suspend fun registrar(medico: Medico): Medico
    suspend fun listar(): List<Medico>
}
