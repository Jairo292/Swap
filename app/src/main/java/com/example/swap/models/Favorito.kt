// ========================================
// Archivo: Favorito.kt
// ========================================
package com.example.swap.models

import java.util.Date

data class Favorito(
    val idFavorito: Int = 0,
    val idUsuarioFk: Int,
    val idPublicacionFk: Int,
    val fechaFavorito: Date = Date()
)