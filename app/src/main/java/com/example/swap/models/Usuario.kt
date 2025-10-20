package com.example.swap.models

/**
 * Modelo de datos para Usuario
 * Corresponde a la tabla 'usuarios' en MySQL
 */
data class Usuario(
    val idUsuario: Int = 0,                    // id_usuario (PK)
    val nombre: String,                        // nombre
    val apellidos: String,                     // apellidos
    val correo: String,                        // correo (UNIQUE)
    val contrasena: String,                    // contrasena
    val telefono: String? = null,              // telefono (opcional)
    val alias: String,                         // alias
    val fotoPerfil: ByteArray? = null          // foto_perfil (BLOB)
) {
    // Override equals y hashCode para comparar ByteArray correctamente
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Usuario

        if (idUsuario != other.idUsuario) return false
        if (correo != other.correo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idUsuario
        result = 31 * result + correo.hashCode()
        return result
    }

    /**
     * Validación de contraseña según requisitos del proyecto:
     * - Mínimo 10 caracteres
     * - Al menos 1 mayúscula
     * - Al menos 1 minúscula
     * - Al menos 1 número
     */
    companion object {
        fun validarContrasena(password: String): Boolean {
            val tieneLongitudMinima = password.length >= 10
            val tieneMayuscula = password.any { it.isUpperCase() }
            val tieneMinuscula = password.any { it.isLowerCase() }
            val tieneNumero = password.any { it.isDigit() }

            return tieneLongitudMinima && tieneMayuscula && tieneMinuscula && tieneNumero
        }
    }
}