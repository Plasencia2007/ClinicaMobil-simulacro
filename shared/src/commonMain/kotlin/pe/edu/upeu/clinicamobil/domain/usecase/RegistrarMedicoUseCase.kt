package pe.edu.upeu.clinicamobil.domain.usecase

import pe.edu.upeu.clinicamobil.domain.model.Medico
import pe.edu.upeu.clinicamobil.domain.repository.MedicoRepository

private val COLEGIATURA_REGEX = Regex("^[0-9]{5,6}$")

data class ErroresDeMedico(
    val nombre: String? = null,
    val colegiatura: String? = null,
    val especialidad: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || colegiatura != null || especialidad != null
}

class MedicoInvalidoException(val errores: ErroresDeMedico) : Exception("Los datos del médico no son válidos")

class RegistrarMedicoUseCase(
    private val repository: MedicoRepository
) {
    suspend operator fun invoke(
        nombre: String,
        colegiatura: String,
        especialidad: String
    ): Result<Medico> {
        val errores = ErroresDeMedico(
            nombre = validarNombre(nombre),
            colegiatura = validarColegiatura(colegiatura),
            especialidad = validarEspecialidad(especialidad)
        )
        if (errores.hayErrores) {
            return Result.failure(MedicoInvalidoException(errores))
        }

        return resultadoDe {
            repository.registrar(
                Medico(
                    id = 0L,
                    nombre = nombre.trim(),
                    colegiatura = colegiatura.trim(),
                    especialidad = especialidad.trim().ifBlank { null }
                )
            )
        }
    }

    private fun validarNombre(nombre: String): String? =
        if (nombre.isBlank()) "El nombre es obligatorio" else null

    private fun validarColegiatura(colegiatura: String): String? {
        val valor = colegiatura.trim()
        return when {
            valor.isBlank() -> "La colegiatura es obligatoria"
            !COLEGIATURA_REGEX.matches(valor) -> "La colegiatura debe tener entre 5 y 6 dígitos"
            else -> null
        }
    }

    private fun validarEspecialidad(especialidad: String): String? {
        val valor = especialidad.trim()
        return if (valor.isNotEmpty() && valor.length < 3) {
            "La especialidad debe tener al menos 3 caracteres"
        } else {
            null
        }
    }
}
