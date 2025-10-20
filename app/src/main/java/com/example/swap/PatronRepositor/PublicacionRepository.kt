package com.example.swap.repository

import android.content.ContentValues
import android.content.Context
import com.example.swap.database.DatabaseHelper
import com.example.swap.models.Publicacion
import com.example.swap.network.ApiService
import java.util.Date

/**
 * PATRÓN REPOSITORY
 *
 * Propósito: Abstraer el origen de los datos (local o remoto) y proporcionar
 * una interfaz única para acceder a ellos.
 *
 * Uso en el proyecto: Gestionar publicaciones tanto desde el servidor (API)
 * como desde la base de datos local (modo offline).
 *
 * Ventajas:
 * - Separa la lógica de acceso a datos de la UI
 * - Facilita el cambio entre fuente de datos (servidor vs local)
 * - Facilita testing y mantenimiento
 */
class PublicacionRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper.getInstance(context)
    private val apiService = ApiService.getInstance() // Retrofit instance

    /**
     * Obtener publicaciones desde el servidor o caché local
     * Implementa lógica de fallback: intenta servidor, si falla usa caché
     */
    suspend fun obtenerPublicaciones(): List<Publicacion> {
        return try {
            // Intentar obtener del servidor
            val publicacionesRemote = apiService.getPublicaciones()

            // Guardar en caché local para uso offline
            guardarEnCache(publicacionesRemote)

            publicacionesRemote
        } catch (e: Exception) {
            // Si falla la conexión, usar caché local
            obtenerPublicacionesLocales()
        }
    }

    /**
     * Guardar publicación como borrador (solo local)
     */
    fun guardarBorrador(publicacion: Publicacion): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_usuario_fk", publicacion.idUsuarioFk)
            put("titulo", publicacion.titulo)
            put("descripcion", publicacion.descripcion)
            put("fecha_creacion", publicacion.fechaCreacion.time)
            put("fecha_edicion", publicacion.fechaEdicion.time)
            put("estado", EstadoPublicacion.BORRADOR.valor)
        }

        return db.insert("publicaciones_borrador", null, values)
    }

    /**
     * Publicar en el servidor
     */
    suspend fun publicarEnServidor(publicacion: Publicacion): Boolean {
        return try {
            apiService.crearPublicacion(publicacion)

            // Si se publicó exitosamente, eliminar borrador local si existe
            if (publicacion.esBorrador) {
                eliminarBorrador(publicacion.id)
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Obtener borradores locales
     */
    fun obtenerBorradores(): List<Publicacion> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "publicaciones_borrador",
            null, null, null, null, null,
            "fecha_modificacion DESC"
        )

        val borradores = mutableListOf<Publicacion>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val usuarioId = getInt(getColumnIndexOrThrow("usuario_id"))
                val titulo = getString(getColumnIndexOrThrow("titulo"))
                val descripcion = getString(getColumnIndexOrThrow("descripcion"))
                val fechaCreacion = Date(getLong(getColumnIndexOrThrow("fecha_creacion")))

                borradores.add(
                    Publicacion(
                        id = id,
                        usuarioId = usuarioId,
                        titulo = titulo,
                        descripcion = descripcion,
                        fechaCreacion = fechaCreacion,
                        esBorrador = true
                    )
                )
            }
            close()
        }

        return borradores
    }

    // Métodos privados auxiliares

    private fun guardarEnCache(publicaciones: List<Publicacion>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()

        try {
            publicaciones.forEach { pub ->
                val values = ContentValues().apply {
                    put("id", pub.id)
                    put("usuario_id", pub.usuarioId)
                    put("titulo", pub.titulo)
                    put("descripcion", pub.descripcion)
                    put("votos_favor", pub.votosAFavor)
                    put("votos_contra", pub.votosEnContra)
                    put("fecha_creacion", pub.fechaCreacion.time)
                }
                db.insertWithOnConflict(
                    "publicaciones_cache",
                    null,
                    values,
                    android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    private fun obtenerPublicacionesLocales(): List<Publicacion> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "publicaciones_cache",
            null, null, null, null, null,
            "fecha_creacion DESC"
        )

        val publicaciones = mutableListOf<Publicacion>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val usuarioId = getInt(getColumnIndexOrThrow("usuario_id"))
                val titulo = getString(getColumnIndexOrThrow("titulo"))
                val descripcion = getString(getColumnIndexOrThrow("descripcion"))
                val votosAFavor = getInt(getColumnIndexOrThrow("votos_favor"))
                val votosEnContra = getInt(getColumnIndexOrThrow("votos_contra"))
                val fechaCreacion = Date(getLong(getColumnIndexOrThrow("fecha_creacion")))

                publicaciones.add(
                    Publicacion(
                        id = id,
                        usuarioId = usuarioId,
                        titulo = titulo,
                        descripcion = descripcion,
                        votosAFavor = votosAFavor,
                        votosEnContra = votosEnContra,
                        fechaCreacion = fechaCreacion,
                        publicadoEnServidor = true
                    )
                )
            }
            close()
        }

        return publicaciones
    }

    private fun eliminarBorrador(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("publicaciones_borrador", "id = ?", arrayOf(id.toString()))
    }
}

/**
 * EJEMPLO DE USO en un ViewModel o Activity:
 *
 * val repository = PublicacionRepository(context)
 *
 * // Obtener publicaciones (intenta servidor, si falla usa caché)
 * lifecycleScope.launch {
 *     val publicaciones = repository.obtenerPublicaciones()
 *     // Actualizar UI
 * }
 *
 * // Guardar borrador
 * val borrador = Publicacion(...)
 * repository.guardarBorrador(borrador)
 *
 * // Publicar
 * lifecycleScope.launch {
 *     val exito = repository.publicarEnServidor(publicacion)
 * }
 */