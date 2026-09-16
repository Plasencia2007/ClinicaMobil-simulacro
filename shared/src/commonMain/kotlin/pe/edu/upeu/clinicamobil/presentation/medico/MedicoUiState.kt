package pe.edu.upeu.clinicamobil.presentation.medico

data class MedicoUiState(
    val fase: FaseListado = FaseListado.Cargando,
    val formulario: FormularioMedico = FormularioMedico(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
) {
    sealed interface FaseListado {
        data object Cargando : FaseListado
        data object SinMedicos : FaseListado
        data class ConMedicos(val medicos: List<MedicoUi>) : FaseListado
        data class Error(val mensaje: String) : FaseListado
    }
}

data class FormularioMedico(
    val nombre: String = "",
    val colegiatura: String = "",
    val especialidad: String = "",
    val errorNombre: String? = null,
    val errorColegiatura: String? = null,
    val errorEspecialidad: String? = null
)
