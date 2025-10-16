package com.example.swap.ui.theme

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.swap.R

class IndexActivity : AppCompatActivity() {

    // Vistas principales
    private lateinit var etSearch: EditText
    private lateinit var scrollFeed: ScrollView
    private lateinit var layoutHistorial: LinearLayout
    private lateinit var layoutResultados: LinearLayout
    private lateinit var tvLogo: TextView
    private lateinit var ivHome: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        // Referencias de vistas
        etSearch = findViewById(R.id.etSearch)
        scrollFeed = findViewById(R.id.scrollFeed)
        layoutHistorial = findViewById(R.id.layoutHistorial)
        layoutResultados = findViewById(R.id.layoutResultados)
        tvLogo = findViewById(R.id.tvLogo)
        ivHome = findViewById(R.id.ivHome)

        // 🔹 Listener: barra de búsqueda (cuando recibe o pierde foco)
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                tvLogo.visibility = View.GONE
                scrollFeed.visibility = View.GONE
                layoutHistorial.visibility = View.VISIBLE
                layoutResultados.visibility = View.GONE
            } else {
                tvLogo.visibility = View.VISIBLE
            }
        }

        // 🔹 Listener: cuando el usuario escribe en el buscador
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    layoutHistorial.visibility = View.VISIBLE
                    layoutResultados.visibility = View.GONE
                } else {
                    layoutHistorial.visibility = View.GONE
                    layoutResultados.visibility = View.VISIBLE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        // 🔹 Listener: botón de “casita” para refrescar y volver al inicio
        ivHome.setOnClickListener {
            etSearch.text.clear()
            etSearch.clearFocus()

            scrollFeed.visibility = View.VISIBLE
            layoutHistorial.visibility = View.GONE
            layoutResultados.visibility = View.GONE
            tvLogo.visibility = View.VISIBLE
        }

        // 🔹 Historial: clic en búsquedas recientes
        val historialOpciones = listOf<TextView>(
            findViewById(R.id.historial1),
            findViewById(R.id.historial2),
            findViewById(R.id.historial3)
        )

        for (item in historialOpciones) {
            item.setOnClickListener {
                val texto = item.text.toString()
                etSearch.setText(texto)
                etSearch.clearFocus()
                layoutHistorial.visibility = View.GONE
                layoutResultados.visibility = View.VISIBLE
                tvLogo.visibility = View.GONE
            }
        }
    }
}
