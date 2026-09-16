# Parte II — Respuestas

## 1. Al centralizar los datos en el servidor del área de salud, ¿qué archivos cambian y cuáles quedan intactos?

Solo cambian dos cosas: se crea una nueva clase en `data/repository` (por ejemplo `PacienteRepositorioRemoto.kt` y `MedicoRepositorioRemoto.kt`) que implemente las mismas interfaces `PacienteRepository`/`MedicoRepository` consumiendo Ktor u otro cliente HTTP, y se cambia una sola línea en `di/AppModule.kt` (el `single<PacienteRepository> { PacienteRepositorioEnMemoria() }` pasa a `single<PacienteRepository> { PacienteRepositorioRemoto(get()) }`, e igual para médicos). Todo lo demás queda intacto: `domain/model` (Paciente, Medico, Atencion...), `domain/repository` (las interfaces), `domain/usecase` (RegistrarPacienteUseCase, ListarPacientesUseCase, etc.) y toda la capa `presentation` (PacienteViewModel, PacienteUiState, PacienteScreen...). Esto es exactamente la regla de dependencia de Clean Architecture: el dominio no depende de nada externo y las capas externas (datos, presentación) dependen *hacia adentro*, del dominio — nunca al revés. Como los casos de uso y los ViewModel solo conocen la interfaz `PacienteRepository`, nunca la clase concreta `PacienteRepositorioEnMemoria`, sustituir la fuente de datos es un cambio contenido en la capa de datos y en el punto único de ensamblado (Koin), sin tocar reglas de negocio ni pantallas.

## 2. ¿En qué clase vive la regla "el DNI debe tener 8 dígitos"? ¿Por qué no debe repetirse en PacienteScreen ni en PacienteViewModel, y qué pasaría si Paciente la relajara?

La regla vive de forma auditable en `RegistrarPacienteUseCase` (el `DNI_REGEX` y `validarDni()`), que es la única clase que decide si un DNI es válido y qué mensaje mostrar; `Paciente.init` repite la misma condición como invariante estructural de último nivel (para que sea imposible construir un `Paciente` inválido desde cualquier punto del código, no solo desde el formulario), pero el lugar donde se audita y se mantiene la regla de negocio es el caso de uso. No debe repetirse en `PacienteScreen` ni en `PacienteViewModel` porque ninguno de los dos vuelve a validar: la pantalla solo pinta el `errorMessage` que ya viene en el `FormularioPaciente`, y el ViewModel solo reenvía los `ErroresDePaciente` que devuelve el caso de uso. Si la regla se repitiera ahí, existirían dos fuentes de verdad que un cambio futuro (por ejemplo, admitir DNI de extranjería con otro formato) podría desincronizar, mostrando un error distinto al que realmente aplica el caso de uso. Si el modelo `Paciente` relajara su `require` de DNI, el registro normal seguiría bloqueando DNIs inválidos porque el caso de uso valida *antes* de construir el `Paciente`; pero cualquier otro punto del código que construya un `Paciente` directamente —una semilla de datos, una prueba, una futura importación masiva— podría crear pacientes con DNI inválido sin que nadie lo note, porque ya no habría ninguna barrera de última línea.

## 3. ¿Qué observaría el personal de admisión si PacienteRepository estuviera registrado como factory en vez de single?

Cada vez que Koin necesite resolver `PacienteRepository` —lo que ocurre, por ejemplo, cada vez que se crea un nuevo `PacienteViewModel` al volver a entrar a la pantalla de Pacientes— con `factory` se construiría una instancia *nueva* de `PacienteRepositorioEnMemoria`, con su lista interna vacía y su contador de id reiniciado en 1. El personal de admisión vería que el padrón de pacientes "se vacía" cada vez que navegan a otra pantalla (Médicos, Historias) y regresan a Pacientes: los pacientes que acababan de registrar desaparecerían, porque ya no están hablando con el mismo objeto en memoria sino con una copia nueva sin datos. Con `single`, en cambio, Koin construye una única instancia del repositorio para toda la vida de la aplicación, así que el padrón persiste mientras la app siga abierta, sin importar cuántas veces se cree y se destruya el `ViewModel` de la pantalla.

---

# Salida de pruebas — `./gradlew :shared:testAndroidHostTest`

30 pruebas ejecutadas (28 nuevas del Reto 02 + 2 del scaffold inicial), **0 fallos**:

- `PacienteTest`: 4 pruebas
- `AtencionTest`: 3 pruebas
- `RegistrarPacienteUseCaseTest`: 8 pruebas
- `RegistrarMedicoUseCaseTest`: 3 pruebas
- `PacienteRepositorioEnMemoriaTest`: 2 pruebas
- `PacienteViewModelTest`: 5 pruebas
- `AppModuleTest`: 3 pruebas
- `SharedCommonTest`, `SharedLogicAndroidHostTest` (scaffold): 2 pruebas

```
> Task :shared:compileAndroidMain UP-TO-DATE
> Task :shared:compileAndroidHostTest UP-TO-DATE
> Task :shared:testAndroidHostTest

BUILD SUCCESSFUL in 3s
33 actionable tasks: 2 executed, 31 up-to-date
```

Resultados por suite (`shared/build/test-results/testAndroidHostTest/*.xml`):

```
PacienteTest                        tests=4 failures=0 errors=0
AtencionTest                        tests=3 failures=0 errors=0
RegistrarPacienteUseCaseTest        tests=8 failures=0 errors=0
RegistrarMedicoUseCaseTest          tests=3 failures=0 errors=0
PacienteRepositorioEnMemoriaTest    tests=2 failures=0 errors=0
PacienteViewModelTest               tests=5 failures=0 errors=0
AppModuleTest                       tests=3 failures=0 errors=0
SharedCommonTest                    tests=1 failures=0 errors=0
SharedLogicAndroidHostTest          tests=1 failures=0 errors=0
```
