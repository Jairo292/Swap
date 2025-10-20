package com.example.swap.repository

import android.content.ContentValues
import android.content.Context
import com.example.swap.database.DatabaseHelper
import com.example.swap.models.EstadoPublicacion
import com.example.swap.models.Publicacion
// import com.example.swap.network.ApiService  // TODO: Descomentar cuando la API esté lista
import java.util.Date

class PublicacionRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper.getInstance(context)
    // private val apiService = ApiService.getInstance() // TODO: Descomentar cuando la API esté lista

    suspend fun obtenerPublicaciones(): List<Publicacion> {
        // --- CÓDIGO DE API TEMPORALMENTE DESACTIVADO ---
        // Por ahora, solo devolveremos los datos de la caché local
        // para que la app compile sin errores.
        return obtenerPublicacionesLocales()

        /* CÓDIGO ORIGINAL (ACTIVAR CUANDO LA API ESTÉ LISTA)
        return try {
            val publicacionesRemote = apiService.getPublicaciones()
            guardarEnCache(publicacionesRemote)
            publicacionesRemote
        } catch (e: Exception) {
            obtenerPublicacionesLocales()
        }
        */
    }

    fun guardarBorrador(publicacion: Publicacion): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_usuario_fk", publicacion.idUsuarioFk)
            put("titulo", publicacion.titulo)
            put("descripcion", publicacion.descripcion)
            put("fecha_creacion", publicacion.fechaCreacion.time)
            put("fecha_edicion", publicacion.fechaEdicion.time)
            put("estado", publicacion.estado.valor) // <-- CORREGIDO: Usar el valor del enum
        }
        return db.insert("publicaciones_borrador", null, values)
    }

    suspend fun publicarEnServidor(publicacion: Publicacion): Boolean {
        // TODO: Implementar la lógica de la API aquí
        // apiService.crearPublicacion(publicacion)

        if (publicacion.estado == EstadoPublicacion.BORRADOR) {
            eliminarBorrador(publicacion.idPublicacion)
        }
        return true // Simular éxito por ahora
    }

    fun obtenerBorradores(): List<Publicacion> {
        val db = dbHelper.readableDatabase
        // <-- CORREGIDO: La columna de fecha en la tabla borrador es fecha_edicion
        val cursor = db.query(
            "publicaciones_borrador",
            null, null, null, null, null,
            "fecha_edicion DESC"
        )

        val borradores = mutableListOf<Publicacion>()

        with(cursor) {
            while (moveToNext()) {
                // <-- CORREGIDO: Nombres de columnas y propiedades
                val id = getInt(getColumnIndexOrThrow("id_publicacion"))
                val usuarioId = getInt(getColumnIndexOrThrow("id_usuario_fk"))
                val titulo = getString(getColumnIndexOrThrow("titulo"))
                val descripcion = getString(getColumnIndexOrThrow("descripcion"))
                val fechaCreacion = Date(getLong(getColumnIndexOrThrow("fecha_creacion")))
                val fechaEdicion = Date(getLong(getColumnIndexOrThrow("fecha_edicion")))

                borradores.add(
                    Publicacion(
                        idPublicacion = id,
                        idUsuarioFk = usuarioId,
                        titulo = titulo,
                        descripcion = descripcion,
                        fechaCreacion = fechaCreacion,
                        fechaEdicion = fechaEdicion,
                        estado = EstadoPublicacion.BORRADOR // <-- CORREGIDO: Se asigna el estado
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
            // Limpiar caché vieja antes de insertar nuevos datos
            db.delete("publicaciones_cache", null, null)

            publicaciones.forEach { pub ->
                val values = ContentValues().apply {
                    // <-- CORREGIDO: Nombres de columnas y propiedades
                    put("id_publicacion", pub.idPublicacion)
                    put("id_usuario_fk", pub.idUsuarioFk)
                    put("titulo", pub.titulo)
                    put("descripcion", pub.descripcion)
                    put("total_likes", pub.totalLikes) // <-- CORREGIDO
                    put("fecha_creacion", pub.fechaCreacion.time)
                    put("estado", pub.estado.valor)
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
                // <-- CORREGIDO: Nombres de columnas y propiedades
                val id = getInt(getColumnIndexOrThrow("id_publicacion"))
                val usuarioId = getInt(getColumnIndexOrThrow("id_usuario_fk"))
                val titulo = getString(getColumnIndexOrThrow("titulo"))
                val descripcion = getString(getColumnIndexOrThrow("descripcion"))
                val totalLikes = getInt(getColumnIndexOrThrow("total_likes"))
                val fechaCreacion = Date(getLong(getColumnIndexOrThrow("fecha_creacion")))
                val estadoStr = getString(getColumnIndexOrThrow("estado"))

                publicaciones.add(
                    Publicacion(
                        idPublicacion = id,
                        idUsuarioFk = usuarioId,
                        titulo = titulo,
                        descripcion = descripcion,
                        totalLikes = totalLikes,
                        fechaCreacion = fechaCreacion,
                        estado = EstadoPublicacion.fromString(estadoStr)
                    )
                )
            }
            close()
        }
        return publicaciones
    }

    private fun eliminarBorrador(id: Int) {
        val db = dbHelper.writableDatabase
        // <-- CORREGIDO: Nombre de la columna
        db.delete("publicaciones_borrador", "id_publicacion = ?", arrayOf(id.toString()))
    }
}