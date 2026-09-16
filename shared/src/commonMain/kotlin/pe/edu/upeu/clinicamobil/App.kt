package pe.edu.upeu.clinicamobil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ClinicaMobilTheme(darkTheme = darkTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "ClinicaMobil",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    DESTINOS.forEach { destino ->
                        NavigationDrawerItem(
                            label = { Text(destino.screen.titulo) },
                            icon = { Icon(destino.icono, contentDescription = null) },
                            selected = pantallaActual == destino.screen,
                            onClick = {
                                pantallaActual = destino.screen
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Modo oscuro", modifier = Modifier.fillMaxWidth().padding(end = 12.dp))
                        Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(pantallaActual.titulo) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    )
                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    when (pantallaActual) {
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
