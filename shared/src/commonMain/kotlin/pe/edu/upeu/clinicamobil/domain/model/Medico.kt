package pe.edu.upeu.clinicamobil.domain.model

private val COLEGIATURA_REGEX = Regex("^[0-9]{5,6}$")

data class Medico(
    val id: Long,
    val nombre: String,
    val colegiatura: String,
    val especialidad: String?
) {
    init {
        require(nombre.isNotBlank()) { "El nombre es obligatorio" }
        require(colegiatura.isNotBlank()) { "La colegiatura es obligatoria" }
        require(COLEGIATURA_REGEX.matches(colegiatura)) { "La colegiatura debe tener entre 5 y 6 dígitos" }
        require(especialidad == null || especialidad.isNotBlank()) {
            "La especialidad debe tener al menos 3 caracteres"
        }
    }
}
