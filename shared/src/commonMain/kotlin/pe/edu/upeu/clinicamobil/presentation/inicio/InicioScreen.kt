package pe.edu.upeu.clinicamobil.presentation.inicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import pe.edu.upeu.clinicamobil.navigation.Screen

private data class AccesoRapido(
    val titulo: String,
    val icono: ImageVector,
    val destino: Screen
)

private val ACCESOS_RAPIDOS = listOf(
    AccesoRapido("Registrar pacientes", Icons.Default.Person, Screen.Pacientes),
    AccesoRapido("Registrar médicos", Icons.Default.MedicalServices, Screen.Medicos),
    AccesoRapido("Revisar historias clínicas", Icons.Default.Assignment, Screen.Historias)
)

/** RF-01: portada + tres accesos rápidos generados desde una lista (no escritos a mano). */
@Composable
fun InicioScreen(onNavegar: (Screen) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.LocalHospital,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "ClinicaMobil",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                "El padrón y el cuerpo clínico del Centro de Salud, en un mismo lugar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text("Qué puedes hacer", style = MaterialTheme.typography.titleMedium)

        ACCESOS_RAPIDOS.forEach { acceso ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavegar(acceso.destino) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = acceso.icono,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        acceso.titulo,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
