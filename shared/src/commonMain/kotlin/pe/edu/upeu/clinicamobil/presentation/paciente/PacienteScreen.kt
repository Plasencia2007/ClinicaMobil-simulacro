package pe.edu.upeu.clinicamobil.presentation.paciente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
fun PacienteScreen(viewModel: PacienteViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Registro de paciente", style = MaterialTheme.typography.titleMedium)

                ValidatedTextField(
                    value = uiState.formulario.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = "Nombre",
                    errorMessage = uiState.formulario.errorNombre
                )
                ValidatedTextField(
                    value = uiState.formulario.dni,
                    onValueChange = viewModel::onDniChange,
                    label = "DNI",
                    errorMessage = uiState.formulario.errorDni,
                    keyboardType = KeyboardType.Number
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ValidatedTextField(
                        value = uiState.formulario.edad,
                        onValueChange = viewModel::onEdadChange,
                        label = "Edad",
                        errorMessage = uiState.formulario.errorEdad,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    ValidatedTextField(
                        value = uiState.formulario.peso,
                        onValueChange = viewModel::onPesoChange,
                        label = "Peso",
                        errorMessage = uiState.formulario.errorPeso,
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    onClick = viewModel::registrar,
                    enabled = !uiState.registrando,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                ) {
                    Text(if (uiState.registrando) "Registrando…" else "Registrar")
                }

                uiState.mensajeExito?.let { mensaje ->
                    MensajeExito(mensaje = mensaje)
                }
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Padrón de pacientes", style = MaterialTheme.typography.titleMedium)
                conteoDe(uiState.fase)?.let { conteo ->
                    Text(conteo, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
            }

            Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                when (val fase = uiState.fase) {
                    is PacienteUiState.FaseListado.Cargando -> {
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

                    is PacienteUiState.FaseListado.SinPacientes -> {
                        EstadoVacio(
                            icono = Icons.Filled.Groups,
                            titulo = "Sin pacientes",
                            descripcion = "Aún no hay pacientes registrados en el padrón."
                        )
                    }

                    is PacienteUiState.FaseListado.ConPacientes -> {
                        fase.pacientes.forEach { paciente ->
                            PacienteItem(paciente)
                        }
                    }

                    is PacienteUiState.FaseListado.Error -> {
                        EstadoVacio(
                            icono = Icons.Filled.ErrorOutline,
                            titulo = "Error",
                            descripcion = fase.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            textoAccion = "Reintentar",
                            onAccion = viewModel::cargarPacientes
                        )
                    }
                }
            }
        }
    }
}

private fun conteoDe(fase: PacienteUiState.FaseListado): String? = when (fase) {
    is PacienteUiState.FaseListado.ConPacientes -> formatearConteo(fase.pacientes.size)
    is PacienteUiState.FaseListado.SinPacientes -> formatearConteo(0)
    else -> null
}

private fun formatearConteo(cantidad: Int): String =
    if (cantidad == 1) "1 paciente" else "$cantidad pacientes"

@Composable
private fun PacienteItem(paciente: PacienteUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = paciente.nombre.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(paciente.nombre, style = MaterialTheme.typography.titleSmall)
                Text(
                    "DNI ${paciente.dni}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    paciente.lineaSecundaria,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (paciente.esPediatrico) {
                Text(
                    text = "Pediátrico",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}
