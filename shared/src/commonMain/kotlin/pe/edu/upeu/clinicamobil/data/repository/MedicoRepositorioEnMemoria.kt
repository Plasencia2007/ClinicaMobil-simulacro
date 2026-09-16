package pe.edu.upeu.clinicamobil.data.repository

import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.clinicamobil.domain.model.Medico
import pe.edu.upeu.clinicamobil.domain.repository.MedicoRepository

class MedicoRepositorioEnMemoria : MedicoRepository {

    private val mutex = Mutex()
    private val medicos = mutableListOf<Medico>()
    private var siguienteId = 1L

    override suspend fun registrar(medico: Medico): Medico {
        delay(Random.nextLong(300, 800))
        return mutex.withLock {
            val conId = medico.copy(id = siguienteId)
            siguienteId += 1
            medicos.add(conId)
            conId
        }
    }

    override suspend fun listar(): List<Medico> {
        delay(Random.nextLong(300, 800))
        return mutex.withLock { medicos.toList() }
    }
}
