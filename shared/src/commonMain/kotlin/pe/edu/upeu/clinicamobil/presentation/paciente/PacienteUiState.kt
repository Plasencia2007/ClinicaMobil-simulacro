package pe.edu.upeu.clinicamobil.presentation.paciente

data class PacienteUiState(
    val fase: FaseListado = FaseListado.Cargando,
    val formulario: FormularioPaciente = FormularioPaciente(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
) {
    sealed interface FaseListado {
        data object Cargando : FaseListado
        data object SinPacientes : FaseListado
        data class ConPacientes(val pacientes: List<PacienteUi>) : FaseListado
        data class Error(val mensaje: String) : FaseListado
    }
}

data class FormularioPaciente(
    val nombre: String = "",
    val dni: String = "",
    val edad: String = "",
    val peso: String = "",
    val errorNombre: String? = null,
    val errorDni: String? = null,
    val errorEdad: String? = null,
    val errorPeso: String? = null
)
