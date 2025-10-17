package com.example.swap.ui.theme

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.swap.R
import de.hdodenhof.circleimageview.CircleImageView

class PerfilActivity : AppCompatActivity() {

    private lateinit var tvVerFavoritos: TextView
    private lateinit var imgPerfil: CircleImageView
    private lateinit var tvEditar: TextView
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var tvCambiarContrasena: TextView
    private lateinit var btnGuardar: Button
    private lateinit var ivHome: ImageView
    private lateinit var ivAdd: ImageView
    private lateinit var ivProfile: ImageView
    private lateinit var tvUsuario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Referencias a las vistas
        tvVerFavoritos = findViewById(R.id.tvVerFavoritos)
        imgPerfil = findViewById(R.id.imgPerfil)
        tvEditar = findViewById(R.id.tvEditar)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCorreo = findViewById(R.id.etCorreo)
        tvCambiarContrasena = findViewById(R.id.tvCambiarContrasena)
        btnGuardar = findViewById(R.id.btnGuardar)
        ivHome = findViewById(R.id.ivHome)
        ivAdd = findViewById(R.id.ivAdd)
        ivProfile = findViewById(R.id.ivProfile)
        tvUsuario = findViewById(R.id.tvUsuario)

        // 🔹 Texto subrayado y clicable
        tvCambiarContrasena.movementMethod = LinkMovementMethod.getInstance()

        // 🔹 Acción: Ver favoritos
        tvVerFavoritos.setOnClickListener {
            Toast.makeText(this, "Abriendo favoritos...", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Acción: Editar perfil
        tvEditar.setOnClickListener {
            etNombre.isEnabled = true
            etApellido.isEnabled = true
            etCorreo.isEnabled = true
            Toast.makeText(this, "Modo edición activado", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Acción: Guardar cambios
        btnGuardar.setOnClickListener {
            etNombre.isEnabled = false
            etApellido.isEnabled = false
            etCorreo.isEnabled = false
            Toast.makeText(this, "Datos guardados correctamente ✅", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Acción: Cambiar contraseña
        tvCambiarContrasena.setOnClickListener {
            Toast.makeText(this, "Función para cambiar contraseña", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Barra inferior
        ivHome.setOnClickListener {
            val intent = Intent(this, IndexActivity::class.java)
            startActivity(intent)
            finish()
        }

        ivAdd.setOnClickListener {
            Toast.makeText(this, "Agregar nueva publicación", Toast.LENGTH_SHORT).show()
        }

        ivProfile.setOnClickListener {
            Toast.makeText(this, "Ya estás en el perfil 😎", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Datos de ejemplo (puedes cargarlos desde BD o SharedPreferences)
        tvUsuario.text = "Jairo-FCFM"
        etNombre.setText("Jairo")
        etApellido.setText("Morales Arguello")
        etCorreo.setText("Jairo@gmail.com")
    }
}
