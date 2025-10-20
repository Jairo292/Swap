// ========================================
// Archivo: Comentario.kt
// ========================================
package com.example.swap.models

import java.util.Date

data class Comentario(
    val idComentario: Int = 0,
    val idPublicacionFk: Int,
    val idUsuarioFk: Int,
    val idComentarioPadreFk: Int? = null,
    val texto: String,
    val fechaCreacion: Date = Date(),
    val likesComentarios: Int = 0,

    // Datos adicionales útiles
    val nombreUsuario: String? = null,
    val respuestas: List<Comentario> = emptyList()
) {
    fun esRespuesta(): Boolean = idComentarioPadreFk != null
    fun tieneRespuestas(): Boolean = respuestas.isNotEmpty()
}