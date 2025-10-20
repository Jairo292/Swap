package com.example.swap.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * PATRÓN SINGLETON
 *
 * Propósito: Garantizar que solo exista una instancia de la base de datos
 * en toda la aplicación, evitando múltiples conexiones.
 *
 * Uso en el proyecto: Gestión de la base de datos local SQLite para
 * funcionalidad offline (borradores, caché de publicaciones).
 */
class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "swap_db"
        private const val DATABASE_VERSION = 1

        // Variable estática que almacena la única instancia
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        /**
         * Método para obtener la instancia única del DatabaseHelper
         * Thread-safe usando double-checked locking
         */
        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseHelper(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tablas para funcionalidad offline (espejo de MySQL)

        // Cache de usuarios
        db?.execSQL("""
            CREATE TABLE usuarios_cache (
                id_usuario INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                correo TEXT UNIQUE NOT NULL,
                telefono TEXT,
                alias TEXT NOT NULL,
                foto_perfil BLOB
            )
        """)

        // Borradores locales (antes de subir al servidor)
        db?.execSQL("""
            CREATE TABLE publicaciones_borrador (
                id_publicacion INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario_fk INTEGER NOT NULL,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                fecha_creacion INTEGER NOT NULL,
                fecha_edicion INTEGER NOT NULL,
                estado TEXT DEFAULT 'borrador'
            )
        """)

        // Cache de publicaciones del servidor (para modo offline)
        db?.execSQL("""
            CREATE TABLE publicaciones_cache (
                id_publicacion INTEGER PRIMARY KEY,
                id_usuario_fk INTEGER NOT NULL,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                total_likes INTEGER DEFAULT 0,
                fecha_creacion INTEGER NOT NULL,
                estado TEXT NOT NULL
            )
        """)

        // Cache de imágenes de borradores locales
        db?.execSQL("""
            CREATE TABLE imagenes_borrador (
                id_imagen INTEGER PRIMARY KEY AUTOINCREMENT,
                id_publicacion_fk INTEGER NOT NULL,
                imagen BLOB NOT NULL,
                descripcion TEXT,
                fecha_creacion INTEGER NOT NULL
            )
        """)

        // Cache de favoritos (sincronización offline)
        db?.execSQL("""
            CREATE TABLE favoritos_cache (
                id_favorito INTEGER PRIMARY KEY,
                id_usuario_fk INTEGER NOT NULL,
                id_publicacion_fk INTEGER NOT NULL,
                fecha_favorito INTEGER NOT NULL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS usuarios_cache")
        db?.execSQL("DROP TABLE IF EXISTS publicaciones_borrador")
        db?.execSQL("DROP TABLE IF EXISTS publicaciones_cache")
        db?.execSQL("DROP TABLE IF EXISTS imagenes_borrador")
        db?.execSQL("DROP TABLE IF EXISTS favoritos_cache")
        onCreate(db)
    }
}

/**
 * EJEMPLO DE USO:
 *
 * // En cualquier Activity o Fragment:
 * val dbHelper = DatabaseHelper.getInstance(context)
 * val db = dbHelper.writableDatabase
 *
 * // Todas las partes de la app usarán la misma instancia
 */