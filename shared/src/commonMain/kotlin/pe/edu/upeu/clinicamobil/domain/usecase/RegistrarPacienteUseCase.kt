package pe.edu.upeu.clinicamobil.domain.usecase

import pe.edu.upeu.clinicamobil.domain.model.Paciente
import pe.edu.upeu.clinicamobil.domain.repository.PacienteRepository

private val DNI_REGEX = Regex("^[0-9]{8}$")

data class ErroresDePaciente(
    val nombre: String? = null,
    val dni: String? = null,
    val edad: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || dni != null || edad != null || peso != null
}

class PacienteInvalidoException(val errores: ErroresDePaciente) : Exception("Los datos del paciente no son válidos")

class RegistrarPacienteUseCase(
    private val repository: PacienteRepository
) {
    suspend operator fun invoke(
        nombre: String,
        dni: String,
        edad: String,
        peso: String
    ): Result<Paciente> {
        val errores = ErroresDePaciente(
            nombre = validarNombre(nombre),
            dni = validarDni(dni),
            edad = validarEdad(edad),
            peso = validarPeso(peso)
        )
        if (errores.hayErrores) {
            return Result.failure(PacienteInvalidoException(errores))
        }

        return resultadoDe {
            repository.registrar(
                Paciente(
                    id = 0L,
                    nombre = nombre.trim(),
                    dni = dni.trim(),
                    edad = edad.trim().toInt(),
                    peso = peso.trim().toDouble()
                )
            )
        }
    }

    private fun validarNombre(nombre: String): String? =
        if (nombre.isBlank()) "El nombre es obligatorio" else null

    private fun validarDni(dni: String): String? {
        val valor = dni.trim()
        return when {
            valor.isBlank() -> "El DNI es obligatorio"
            !DNI_REGEX.matches(valor) -> "El DNI debe tener 8 dígitos"
            else -> null
        }
    }

    private fun validarEdad(edad: String): String? {
        val valor = edad.trim()
        val numero = valor.toIntOrNull()
        return when {
            valor.isBlank() -> "La edad es obligatoria"
            numero == null -> "La edad debe ser un número entero"
            numero !in 0..Paciente.EDAD_MAXIMA -> "La edad debe estar entre 0 y 120"
            else -> null
        }
    }

    private fun validarPeso(peso: String): String? {
        val valor = peso.trim()
        val numero = valor.toDoubleOrNull()
        return when {
            valor.isBlank() -> "El peso es obligatorio"
            numero == null || numero.isNaN() || numero.isInfinite() -> "El peso debe ser un número válido"
            numero <= 0 -> "El peso debe ser mayor a 0"
            else -> null
        }
    }
}
