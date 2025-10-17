package com.example.swap.ui.theme

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.swap.R

class CambiarContrasenaActivity : AppCompatActivity() {

    private lateinit var etNuevaContrasena: EditText
    private lateinit var etConfirmarContrasena: EditText
    private lateinit var btnGuardarCambios: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena)

        // Referencias
        etNuevaContrasena = findViewById(R.id.etNuevaContrasena)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios)

        // Listener del botón
        btnGuardarCambios.setOnClickListener {
            val nueva = etNuevaContrasena.text.toString().trim()
            val confirmar = etConfirmarContrasena.text.toString().trim()

            if (nueva.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this, "Por favor llena ambos campos", Toast.LENGTH_SHORT).show()
            } else if (nueva != confirmar) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Contraseña actualizada correctamente ✅", Toast.LENGTH_SHORT).show()
                finish() // cierra la ventana y regresa
            }
        }
    }
}
