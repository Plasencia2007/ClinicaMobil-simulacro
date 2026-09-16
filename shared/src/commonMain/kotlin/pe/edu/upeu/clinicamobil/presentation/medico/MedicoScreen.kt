package pe.edu.upeu.clinicamobil.presentation.medico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.clinicamobil.presentation.components.EstadoVacio
import pe.edu.upeu.clinicamobil.presentation.components.MensajeExito
import pe.edu.upeu.clinicamobil.presentation.components.ValidatedTextField

@Composable
fun MedicoScreen(viewModel: MedicoViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Registro de médico", style = MaterialTheme.typography.titleMedium)

                ValidatedTextField(
                    value = uiState.formulario.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = "Nombre",
                    errorMessage = uiState.formulario.errorNombre
                )
                ValidatedTextField(
                    value = uiState.formulario.colegiatura,
                    onValueChange = viewModel::onColegiaturaChange,
                    label = "Colegiatura",
                    errorMessage = uiState.formulario.errorColegiatura,
                    keyboardType = KeyboardType.Number
                )
                ValidatedTextField(
                    value = uiState.formulario.especialidad,
                    onValueChange = viewModel::onEspecialidadChange,
                    label = "Especialidad (opcional)",
                    errorMessage = uiState.formulario.errorEspecialidad
                )

                Button(
                    onClick = viewModel::registrar,
                    enabled = !uiState.registrando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (uiState.registrando) "Registrando…" else "Registrar")
                }

                uiState.mensajeExito?.let { mensaje ->
                    MensajeExito(mensaje = mensaje)
                }
            }
        }

        Column(modifier = Modifier.padding(top = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Cuerpo médico", style = MaterialTheme.typography.titleMedium)
                conteoDe(uiState.fase)?.let { conteo ->
                    Text(conteo, style = MaterialTheme.typography.labelLarge)
                }
            }

            Column(modifier = Modifier.padding(top = 12.dp)) {
                when (val fase = uiState.fase) {
                    is MedicoUiState.FaseListado.Cargando -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Text("Cargando…", modifier = Modifier.padding(top = 8.dp))
                            }
                        }
                    }

                    is MedicoUiState.FaseListado.SinMedicos -> {
                        EstadoVacio(
                            icono = Icons.Filled.MedicalServices,
                            titulo = "Sin médicos",
                            descripcion = "Aún no hay médicos registrados en el cuerpo clínico."
                        )
                    }

                    is MedicoUiState.FaseListado.ConMedicos -> {
                        fase.medicos.forEach { medico ->
                            MedicoItem(medico)
                        }
                    }

                    is MedicoUiState.FaseListado.Error -> {
                        EstadoVacio(
                            icono = Icons.Filled.ErrorOutline,
                            titulo = "Error",
                            descripcion = fase.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            textoAccion = "Reintentar",
                            onAccion = viewModel::cargarMedicos
                        )
                    }
                }
            }
        }
    }
}

private fun conteoDe(fase: MedicoUiState.FaseListado): String? = when (fase) {
    is MedicoUiState.FaseListado.ConMedicos -> formatearConteo(fase.medicos.size)
    is MedicoUiState.FaseListado.SinMedicos -> formatearConteo(0)
    else -> null
}

private fun formatearConteo(cantidad: Int): String =
    if (cantidad == 1) "1 médico" else "$cantidad médicos"

@Composable
private fun MedicoItem(medico: MedicoUi) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(medico.nombre, style = MaterialTheme.typography.titleSmall)
            Text(medico.lineaSecundaria, style = MaterialTheme.typography.bodySmall)
        }
    }
}
