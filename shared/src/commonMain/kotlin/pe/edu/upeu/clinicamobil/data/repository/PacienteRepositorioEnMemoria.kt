package pe.edu.upeu.clinicamobil.data.repository

import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.clinicamobil.domain.model.Paciente
import pe.edu.upeu.clinicamobil.domain.repository.PacienteRepository

class PacienteRepositorioEnMemoria : PacienteRepository {

    private val mutex = Mutex()
    private val pacientes = mutableListOf<Paciente>()
    private var siguienteId = 1L

    override suspend fun registrar(paciente: Paciente): Paciente {
        delay(Random.nextLong(300, 800))
        return mutex.withLock {
            val conId = paciente.copy(id = siguienteId)
            siguienteId += 1
            pacientes.add(conId)
            conId
        }
    }

    override suspend fun listar(): List<Paciente> {
        delay(Random.nextLong(300, 800))
        return mutex.withLock { pacientes.toList() }
    }
}
