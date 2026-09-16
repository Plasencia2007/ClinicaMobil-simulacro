package pe.edu.upeu.clinicamobil.presentation.historia

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pe.edu.upeu.clinicamobil.presentation.components.EstadoVacio

/** RF-05: el módulo de historias clínicas queda preparado para una versión posterior. */
@Composable
fun HistoriasScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EstadoVacio(
            icono = Icons.Filled.Assignment,
            titulo = "Historias clínicas en construcción",
            descripcion = "Este módulo estará disponible en una próxima versión de ClinicaMobil."
        )
    }
}
