package pe.edu.upeu.clinicamobil.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class Destino(val screen: Screen, val icono: ImageVector)

/** Única fuente de verdad para el menú lateral y el título de la barra superior. */
val DESTINOS = listOf(
    Destino(Screen.Inicio, Icons.Default.Home),
    Destino(Screen.Pacientes, Icons.Default.Person),
    Destino(Screen.Medicos, Icons.Default.MedicalServices),
    Destino(Screen.Historias, Icons.Default.Assignment)
)
