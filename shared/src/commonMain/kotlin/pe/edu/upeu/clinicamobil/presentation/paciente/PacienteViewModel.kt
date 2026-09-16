package pe.edu.upeu.clinicamobil.presentation.paciente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.clinicamobil.domain.usecase.ErroresDePaciente
import pe.edu.upeu.clinicamobil.domain.usecase.ListarPacientesUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.PacienteInvalidoException
import pe.edu.upeu.clinicamobil.domain.usecase.RegistrarPacienteUseCase

class PacienteViewModel(
    private val registrarPaciente: RegistrarPacienteUseCase,
    private val listarPacientes: ListarPacientesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PacienteUiState())
    val uiState: StateFlow<PacienteUiState> = _uiState.asStateFlow()

    init {
        cargarPacientes()
    }

    fun cargarPacientes() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = PacienteUiState.FaseListado.Cargando) }
            listarPacientes()
                .onSuccess { pacientes ->
                    _uiState.update {
                        it.copy(
                            fase = if (pacientes.isEmpty()) {
                                PacienteUiState.FaseListado.SinPacientes
                            } else {
                                PacienteUiState.FaseListado.ConPacientes(pacientes.map { p -> p.aUi() })
                            }
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = PacienteUiState.FaseListado.Error(
                                error.message ?: "No se pudo cargar el padrón de pacientes"
                            )
                        )
                    }
                }
        }
    }

    fun onNombreChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(nombre = valor, errorNombre = null)) }
    }

    fun onDniChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(dni = valor, errorDni = null)) }
    }

    fun onEdadChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(edad = valor, errorEdad = null)) }
    }

    fun onPesoChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(peso = valor, errorPeso = null)) }
    }

    fun registrar() {
        if (_uiState.value.registrando) return
        val formulario = _uiState.value.formulario

        viewModelScope.launch {
            _uiState.update { it.copy(registrando = true) }
            registrarPaciente(
                nombre = formulario.nombre,
                dni = formulario.dni,
                edad = formulario.edad,
                peso = formulario.peso
            ).onSuccess { paciente ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = FormularioPaciente(),
                        mensajeExito = "Paciente \"${paciente.nombre}\" registrado correctamente"
                    )
                }
                cargarPacientes()
            }.onFailure { error ->
                val errores = (error as? PacienteInvalidoException)?.errores ?: ErroresDePaciente()
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = it.formulario.copy(
                            errorNombre = errores.nombre,
                            errorDni = errores.dni,
                            errorEdad = errores.edad,
                            errorPeso = errores.peso
                        )
                    )
                }
            }
        }
    }

    fun consumirMensajeExito() {
        _uiState.update { it.copy(mensajeExito = null) }
    }
}
