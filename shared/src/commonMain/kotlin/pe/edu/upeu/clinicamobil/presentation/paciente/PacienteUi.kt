package pe.edu.upeu.clinicamobil.presentation.paciente

import pe.edu.upeu.clinicamobil.domain.model.Paciente

data class PacienteUi(
    val id: Long,
    val nombre: String,
    val dni: String,
    val lineaSecundaria: String,
    val esPediatrico: Boolean
)

/** Arma la línea secundaria del Anexo B: "34 años · 70.5 kg" (singular "1 año", peso con un decimal). */
fun Paciente.aUi(): PacienteUi {
    val unidadEdad = if (edad == 1) "año" else "años"
    return PacienteUi(
        id = id,
        nombre = nombre,
        dni = dni,
        lineaSecundaria = "$edad $unidadEdad · ${formatearUnDecimal(peso)} kg",
        esPediatrico = esPediatrico
    )
}

private fun formatearUnDecimal(valor: Double): String {
    val redondeado = kotlin.math.round(valor * 10) / 10.0
    val parteEntera = redondeado.toLong()
    val parteDecimal = kotlin.math.abs(((redondeado - parteEntera) * 10).toInt())
    return "$parteEntera.$parteDecimal"
}
