package pe.edu.upeu.clinicamobil.di

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame
import org.koin.dsl.koinApplication
import pe.edu.upeu.clinicamobil.data.repository.PacienteRepositorioEnMemoria
import pe.edu.upeu.clinicamobil.domain.repository.PacienteRepository
import pe.edu.upeu.clinicamobil.domain.usecase.ListarMedicosUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.ListarPacientesUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.RegistrarMedicoUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.RegistrarPacienteUseCase

class AppModuleTest {

    private fun crearKoin() = koinApplication {
        modules(platformModule, dataModule, domainModule, presentationModule)
    }.koin

    @Test
    fun resuelvePacienteRepositoryComoPacienteRepositorioEnMemoria() {
        val repository = crearKoin().get<PacienteRepository>()
        assertIs<PacienteRepositorioEnMemoria>(repository)
    }

    @Test
    fun elRepositorioDePacientesEsUnico() {
        val koin = crearKoin()
        assertSame(koin.get<PacienteRepository>(), koin.get<PacienteRepository>())
    }

    @Test
    fun resuelveLosCuatroCasosDeUso() {
        val koin = crearKoin()
        koin.get<RegistrarPacienteUseCase>()
        koin.get<ListarPacientesUseCase>()
        koin.get<RegistrarMedicoUseCase>()
        koin.get<ListarMedicosUseCase>()
    }
}
