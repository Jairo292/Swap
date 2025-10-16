package com.example.swap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.swap.ui.theme.LoginFormActivity
import com.example.swap.ui.theme.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Botón “Iniciar Sesión” → abre el formulario
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginFormActivity::class.java)
            startActivity(intent)
        }

        // Texto “Registrarse” → abre la pantalla de registro
        val tvRegister = findViewById<TextView>(R.id.tvRegisterLink)
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
