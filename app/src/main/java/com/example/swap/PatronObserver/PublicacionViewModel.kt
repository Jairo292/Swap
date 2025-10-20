package com.example.swap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.swap.models.Publicacion
import com.example.swap.repository.PublicacionRepository
import kotlinx.coroutines.launch


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
            if (pub.idPublicacion == publicacionId) {
                pub.copy(totalLikes = pub.totalLikes + 1)
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

