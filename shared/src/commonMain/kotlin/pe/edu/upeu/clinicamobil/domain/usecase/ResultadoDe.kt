package pe.edu.upeu.clinicamobil.domain.usecase

import kotlinx.coroutines.CancellationException

/**
 * Ejecuta [bloque] y lo envuelve en un [Result], preservando la cancelación
 * estructurada de corrutinas: CancellationException nunca se convierte en
 * Result.failure, se vuelve a lanzar tal como exige la cancelación cooperativa.
 */
suspend fun <T> resultadoDe(bloque: suspend () -> T): Result<T> {
    return try {
        Result.success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
