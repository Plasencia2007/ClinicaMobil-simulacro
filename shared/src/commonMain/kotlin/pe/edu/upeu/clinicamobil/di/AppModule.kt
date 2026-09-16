package pe.edu.upeu.clinicamobil.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import pe.edu.upeu.clinicamobil.data.repository.MedicoRepositorioEnMemoria
import pe.edu.upeu.clinicamobil.data.repository.PacienteRepositorioEnMemoria
import pe.edu.upeu.clinicamobil.domain.repository.MedicoRepository
import pe.edu.upeu.clinicamobil.domain.repository.PacienteRepository
import pe.edu.upeu.clinicamobil.domain.usecase.ListarMedicosUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.ListarPacientesUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.RegistrarMedicoUseCase
import pe.edu.upeu.clinicamobil.domain.usecase.RegistrarPacienteUseCase
import pe.edu.upeu.clinicamobil.presentation.medico.MedicoViewModel
import pe.edu.upeu.clinicamobil.presentation.paciente.PacienteViewModel

val dataModule = module {
    single<PacienteRepository> { PacienteRepositorioEnMemoria() }
    single<MedicoRepository> { MedicoRepositorioEnMemoria() }
}

val domainModule = module {
    factory { RegistrarPacienteUseCase(get()) }
    factory { ListarPacientesUseCase(get()) }
    factory { RegistrarMedicoUseCase(get()) }
    factory { ListarMedicosUseCase(get()) }
}

val presentationModule = module {
    viewModel { PacienteViewModel(get(), get()) }
    viewModel { MedicoViewModel(get(), get()) }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(platformModule, dataModule, domainModule, presentationModule)
    }
}
