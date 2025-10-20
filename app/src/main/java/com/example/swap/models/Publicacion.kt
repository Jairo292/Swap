// ========================================
// Archivo: Publicacion.kt
// ========================================
package com.example.swap.models

import java.util.Date

data class Publicacion(
    val idPublicacion: Int = 0,
    val idUsuarioFk: Int,
    val titulo: String,
    val descripcion: String,
    val fechaCreacion: Date = Date(),
    val fechaEdicion: Date = Date(),
    val estado: EstadoPublicacion = EstadoPublicacion.BORRADOR,

    // Relaciones (no están en la tabla directamente, pero útiles en la app)
    val imagenes: List<ImagenPublicacion> = emptyList(),
    val comentarios: List<Comentario> = emptyList(),
    val totalLikes: Int = 0,
    val usuario: Usuario? = null
)

enum class EstadoPublicacion(val valor: String) {
    BORRADOR("borrador"),
    PUBLICADO("publicado");

    companion object {
        fun fromString(valor: String): EstadoPublicacion {
            return values().find { it.valor == valor } ?: BORRADOR
        }
    }
}