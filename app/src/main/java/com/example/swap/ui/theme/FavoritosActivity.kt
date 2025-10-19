package com.example.swap.ui.theme

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.swap.R

class FavoritosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        val ivBack = findViewById<ImageView>(R.id.ivBackFavoritos)
        ivBack.setOnClickListener {
            finish() // Regresa a la pantalla anterior
        }
    }
}
