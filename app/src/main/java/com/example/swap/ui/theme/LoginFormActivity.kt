package com.example.swap.ui.theme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swap.databinding.ActivityLoginFormBinding
import com.example.swap.ui.theme.IndexActivity

class LoginFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Botón “Siguiente” → ir a siguiente actividad (ejemplo)
        binding.btnSiguiente.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Ingresa tu correo"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Ingresa tu contraseña"
                return@setOnClickListener
            }

            // Si pasa validación, continúa
            val intent = Intent(this, IndexActivity::class.java)
            startActivity(intent)
        }
    }
}
