package com.example.swap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.swap.models.Publicacion
import com.example.swap.repository.PublicacionRepository
import kotlinx.coroutines.launch

/**
 * PATRÓN OBSERVER (implementado con LiveData)
 *
 * Propósito: Establecer una relación de dependencia uno-a-muchos entre objetos,
 * de manera que cuando un objeto cambie de estado, todos sus dependientes
 * sean notificados automáticamente.
 *
 * Uso en el proyecto: Cuando los datos cambian (nuevas publicaciones, likes, etc.),
 * todas las vistas que observan estos datos se actualizan automáticamente.
 *
 * Ventajas:
 * - Las Activities/Fragments no necesitan pedir datos constantemente
 * - Actualización automática de la UI cuando cambian los datos
 * - Respeta el ciclo de vida de Android (evita memory leaks)
 */
class PublicacionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PublicacionRepository(application)

    // OBSERVABLES - Los datos que serán observados por la UI

    // LiveData privado (mutable) - solo el ViewModel puede modificarlo
    private val _publicaciones = MutableLiveData<List<Publicacion>>()
    // LiveData público (inmutable) - las Activities/Fragments solo pueden observar
    val publicaciones: LiveData<List<Publicacion>> = _publicaciones

    private val _borradores = MutableLiveData<List<Publicacion>>()
    val borradores: LiveData<List<Publicacion>> = _borradores

    private val _estadoCarga = MutableLiveData<EstadoCarga>()
    val estadoCarga: LiveData<EstadoCarga> = _estadoCarga

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> = _mensajeError

    /**
     * Cargar publicaciones del servidor o caché
     * Automáticamente notificará a todos los observadores cuando termine
     */
    fun cargarPublicaciones() {
        viewModelScope.launch {
            try {
                _estadoCarga.value = EstadoCarga.CARGANDO

                val pubs = repository.obtenerPublicaciones()

                // Al cambiar el valor, TODOS los observadores son notificados
                _publicaciones.value = pubs
                _estadoCarga.value = EstadoCarga.EXITOSO

            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar publicaciones: ${e.message}"
                _estadoCarga.value = EstadoCarga.ERROR
            }
        }
    }

    /**
     * Cargar borradores locales
     */
    fun cargarBorradores() {
        viewModelScope.launch {
            val borradores = repository.obtenerBorradores()
            _borradores.value = borradores // Notifica a los observadores
        }
    }

    /**
     * Guardar nuevo borrador
     */
    fun guardarBorrador(publicacion: Publicacion) {
        viewModelScope.launch {
            repository.guardarBorrador(publicacion)

            // Recargar lista de borradores para reflejar el cambio
            cargarBorradores()
        }
    }

    /**
     * Publicar en el servidor
     */
    fun publicar(publicacion: Publicacion) {
        viewModelScope.launch {
            try {
                _estadoCarga.value = EstadoCarga.CARGANDO

                val exito = repository.publicarEnServidor(publicacion)

                if (exito) {
                    _estadoCarga.value = EstadoCarga.EXITOSO
                    // Recargar publicaciones para incluir la nueva
                    cargarPublicaciones()
                    // Recargar borradores por si se eliminó alguno
                    cargarBorradores()
                } else {
                    _mensajeError.value = "No se pudo publicar"
                    _estadoCarga.value = EstadoCarga.ERROR
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error: ${e.message}"
                _estadoCarga.value = EstadoCarga.ERROR
            }
        }
    }

    /**
     * Agregar like a una publicación
     * Actualiza el modelo y notifica a los observadores
     */
    fun darLike(publicacionId: Int) {
        val publicacionesActuales = _publicaciones.value ?: return

        val publicacionesActualizadas = publicacionesActuales.map { pub ->
            if (pub.id == publicacionId) {
                pub.copy(votosAFavor = pub.votosAFavor + 1)
            } else {
                pub
            }
        }

        // Al actualizar, todos los observadores se actualizan automáticamente
        _publicaciones.value = publicacionesActualizadas

        // Aquí también sincronizarías con el servidor
        viewModelScope.launch {
            // apiService.darLike(publicacionId)
        }
    }

    enum class EstadoCarga {
        CARGANDO,
        EXITOSO,
        ERROR
    }
}

/**
 * EJEMPLO DE USO en una Activity:
 *
 * class IndexActivity : AppCompatActivity() {
 *
 *     private val viewModel: PublicacionViewModel by viewModels()
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContentView(R.layout.activity_index)
 *
 *         // OBSERVAR los cambios en las publicaciones
 *         viewModel.publicaciones.observe(this) { publicaciones ->
 *             // Este código se ejecuta automáticamente cuando cambian los datos
 *             actualizarUI(publicaciones)
 *         }
 *
 *         // OBSERVAR el estado de carga
 *         viewModel.estadoCarga.observe(this) { estado ->
 *             when (estado) {
 *                 EstadoCarga.CARGANDO -> mostrarLoading()
 *                 EstadoCarga.EXITOSO -> ocultarLoading()
 *                 EstadoCarga.ERROR -> mostrarError()
 *             }
 *         }
 *
 *         // OBSERVAR mensajes de error
 *         viewModel.mensajeError.observe(this) { mensaje ->
 *             mensaje?.let {
 *                 Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
 *             }
 *         }
 *
 *         // Cargar datos iniciales
 *         viewModel.cargarPublicaciones()
 *     }
 *
 *     private fun actualizarUI(publicaciones: List<Publicacion>) {
 *         // Actualizar RecyclerView o UI
 *     }
 * }
 *
 * VENTAJA CLAVE: La Activity no necesita "jalar" los datos constantemente.
 * Los datos "empujan" automáticamente las actualizaciones a la UI cuando cambian.
 */