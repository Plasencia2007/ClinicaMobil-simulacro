package pe.edu.upeu.clinicamobil.navigation

sealed class Screen(val titulo: String, val clave: String) {
    data object Inicio : Screen("Inicio", "inicio")
    data object Pacientes : Screen("Pacientes", "pacientes")
    data object Medicos : Screen("Médicos", "medicos")
    data object Historias : Screen("Historias", "historias")

    companion object {
        fun desdeClave(clave: String): Screen = when (clave) {
            "pacientes" -> Pacientes
            "medicos" -> Medicos
            "historias" -> Historias
            else -> Inicio
        }
    }
}
