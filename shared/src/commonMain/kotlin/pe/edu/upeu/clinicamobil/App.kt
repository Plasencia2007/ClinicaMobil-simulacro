package pe.edu.upeu.clinicamobil

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.clinicamobil.navigation.DESTINOS
import pe.edu.upeu.clinicamobil.navigation.Screen
import pe.edu.upeu.clinicamobil.presentation.historia.HistoriasScreen
import pe.edu.upeu.clinicamobil.presentation.inicio.InicioScreen
import pe.edu.upeu.clinicamobil.presentation.medico.MedicoScreen
import pe.edu.upeu.clinicamobil.presentation.medico.MedicoViewModel
import pe.edu.upeu.clinicamobil.presentation.paciente.PacienteScreen
import pe.edu.upeu.clinicamobil.presentation.paciente.PacienteViewModel
import pe.edu.upeu.clinicamobil.presentation.theme.ClinicaMobilTheme

private val ScreenSaver: Saver<Screen, String> = Saver(
    save = { it.clave },
    restore = { Screen.desdeClave(it) }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() = KoinContext {
    var darkTheme by rememberSaveable { mutableStateOf(false) }
    var pantallaActual by rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.Inicio) }

    ClinicaMobilTheme(darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(pantallaActual.titulo) },
                    actions = {
                        IconButton(onClick = { darkTheme = !darkTheme }) {
                            Icon(
                                imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Cambiar tema"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                    DESTINOS.forEach { destino ->
                        NavigationBarItem(
                            selected = pantallaActual == destino.screen,
                            onClick = { pantallaActual = destino.screen },
                            icon = { Icon(destino.icono, contentDescription = null) },
                            label = { Text(destino.screen.titulo) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                AnimatedContent(
                    targetState = pantallaActual,
                    label = "navegacion",
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { pantalla ->
                    when (pantalla) {
                        is Screen.Inicio -> InicioScreen(onNavegar = { pantallaActual = it })
                        is Screen.Pacientes -> {
                            val viewModel = koinViewModel<PacienteViewModel>()
                            PacienteScreen(viewModel = viewModel)
                        }
                        is Screen.Medicos -> {
                            val viewModel = koinViewModel<MedicoViewModel>()
                            MedicoScreen(viewModel = viewModel)
                        }
                        is Screen.Historias -> HistoriasScreen()
                    }
                }
            }
        }
    }
}
