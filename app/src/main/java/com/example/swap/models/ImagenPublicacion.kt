// ========================================
// Archivo: ImagenPublicacion.kt
// ========================================
package com.example.swap.models

import java.util.Date

data class ImagenPublicacion(
    val idImagen: Int = 0,
    val idPublicacionFk: Int,
    val imagen: ByteArray,
    val descripcion: String? = null,
    val fechaCreacion: Date = Date(),
    val fechaEdicion: Date = Date()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ImagenPublicacion
        if (idImagen != other.idImagen) return false
        return true
    }

    override fun hashCode(): Int {
        return idImagen
    }
}