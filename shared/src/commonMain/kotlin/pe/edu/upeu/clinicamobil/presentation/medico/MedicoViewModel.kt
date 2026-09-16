package pe.edu.upeu.clinicamobil.presentation.medico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.clinicamobil.domain.usecase.ErroresDeMedico
import pe.edu.upeu.clinicamobil.domain.usecase.ListarMedicosUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.MedicoInvalidoException
import pe.edu.upeu.clinicamobil.domain.usecase.RegistrarMedicoUseCase

class MedicoViewModel(
    private val registrarMedico: RegistrarMedicoUseCase,
    private val listarMedicos: ListarMedicosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicoUiState())
    val uiState: StateFlow<MedicoUiState> = _uiState.asStateFlow()

    init {
        cargarMedicos()
    }

    fun cargarMedicos() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = MedicoUiState.FaseListado.Cargando) }
            listarMedicos()
                .onSuccess { medicos ->
                    _uiState.update {
                        it.copy(
                            fase = if (medicos.isEmpty()) {
                                MedicoUiState.FaseListado.SinMedicos
                            } else {
                                MedicoUiState.FaseListado.ConMedicos(medicos.map { m -> m.aUi() })
                            }
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = MedicoUiState.FaseListado.Error(
                                error.message ?: "No se pudo cargar el cuerpo médico"
                            )
                        )
                    }
                }
        }
    }

    fun onNombreChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(nombre = valor, errorNombre = null)) }
    }

    fun onColegiaturaChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(colegiatura = valor, errorColegiatura = null)) }
    }

    fun onEspecialidadChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(especialidad = valor, errorEspecialidad = null)) }
    }

    fun registrar() {
        if (_uiState.value.registrando) return
        val formulario = _uiState.value.formulario

        viewModelScope.launch {
            _uiState.update { it.copy(registrando = true) }
            registrarMedico(
                nombre = formulario.nombre,
                colegiatura = formulario.colegiatura,
                especialidad = formulario.especialidad
            ).onSuccess { medico ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = FormularioMedico(),
                        mensajeExito = "Médico \"${medico.nombre}\" registrado correctamente"
                    )
                }
                cargarMedicos()
            }.onFailure { error ->
                val errores = (error as? MedicoInvalidoException)?.errores ?: ErroresDeMedico()
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = it.formulario.copy(
                            errorNombre = errores.nombre,
                            errorColegiatura = errores.colegiatura,
                            errorEspecialidad = errores.especialidad
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
