package pe.edu.upeu.clinicamobil.domain.usecase

import pe.edu.upeu.clinicamobil.domain.model.Paciente
import pe.edu.upeu.clinicamobil.domain.repository.PacienteRepository

class ListarPacientesUseCase(
    private val repository: PacienteRepository
) {
    suspend operator fun invoke(): Result<List<Paciente>> = resultadoDe { repository.listar() }
}
