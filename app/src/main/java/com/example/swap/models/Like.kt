// ========================================
// Archivo: Like.kt
// ========================================
package com.example.swap.models

import java.util.Date

data class Like(
    val idLike: Int = 0,
    val idUsuarioFk: Int,
    val idPublicacionFk: Int,
    val fecha: Date = Date()
)