package com.example.swap.ui.theme

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.example.swap.R
import com.example.swap.ui.theme.Register2Activity


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Asegúrate de que el XML se llame igual

        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)

        // 👁️ Alternar visibilidad de la contraseña
        etContrasena.setOnTouchListener { _, event ->
            val drawableEnd = 2 // posición del ícono a la derecha
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (etContrasena.right - etContrasena.compoundDrawables[drawableEnd].bounds.width())) {
                    if (etContrasena.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        // Si la contraseña está visible, la ocultamos
                        etContrasena.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        etContrasena.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_visibility_off, 0
                        )
                    } else {
                        // Si está oculta, la mostramos
                        etContrasena.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        etContrasena.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_visibility, 0
                        )
                    }
                    etContrasena.setSelection(etContrasena.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // ✨ Animación: línea azul
        val linea = findViewById<View>(R.id.titleUnderline)
        val animLinea = AnimationUtils.loadAnimation(this, R.anim.anim_linea_extender)
        linea.postDelayed({
            linea.startAnimation(animLinea)
        }, 200)

        // ✨ Animación: título
        val tvTitleRegister = findViewById<View>(R.id.tvTitleRegister)
        val animTitle = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in_left_title)
        tvTitleRegister.startAnimation(animTitle)

        // ✨ Animación: campos
        val fieldsContainer = findViewById<View>(R.id.fieldsContainer)
        val animFields = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in_up_fields)
        fieldsContainer.startAnimation(animFields)

        // ✨ Animación: botón
        val animButton = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in_up_button)
        btnSiguiente.startAnimation(animButton)

        // 🚀 Navegación a la siguiente ventana (Register2Activity)
        btnSiguiente.setOnClickListener {
            val intent = Intent(this, Register2Activity::class.java)
            startActivity(intent)
        }
    }
}
