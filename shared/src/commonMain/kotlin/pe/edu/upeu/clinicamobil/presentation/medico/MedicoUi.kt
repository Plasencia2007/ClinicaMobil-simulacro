package pe.edu.upeu.clinicamobil.presentation.medico

import pe.edu.upeu.clinicamobil.domain.model.Medico

data class MedicoUi(
    val id: Long,
    val nombre: String,
    val colegiatura: String,
    val lineaSecundaria: String
)

/** Arma la línea secundaria del Anexo B: "CMP 12345 · Cardiología"; sin especialidad: "Medicina general". */
fun Medico.aUi(): MedicoUi = MedicoUi(
    id = id,
    nombre = nombre,
    colegiatura = colegiatura,
    lineaSecundaria = "CMP $colegiatura · ${especialidad ?: "Medicina general"}"
)
