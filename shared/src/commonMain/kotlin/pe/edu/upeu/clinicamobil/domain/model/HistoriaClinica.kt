package pe.edu.upeu.clinicamobil.domain.model

data class HistoriaClinica(
    val id: Long,
    val paciente: Paciente,
    val atenciones: List<Atencion>,
    val estado: EstadoHistoria
)
