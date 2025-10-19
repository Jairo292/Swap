package com.example.swap.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swap.R

class BorradoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borradores)

        val ivBack = findViewById<android.widget.ImageView>(R.id.ivBackBorradores)
        val recyclerView = findViewById<RecyclerView>(R.id.rvBorradores)

        ivBack.setOnClickListener {
            finish()
        }

        // Ejemplo temporal
        val borradores = listOf("A", "Mi segundo post", "Post sobre diseño")
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BorradoresAdapter(borradores)
    }
}

class BorradoresAdapter(private val lista: List<String>) :
    RecyclerView.Adapter<BorradoresAdapter.ViewHolder>() {

    inner class ViewHolder(val view: android.view.View) :
        RecyclerView.ViewHolder(view) {
        val titulo = view.findViewById<android.widget.TextView>(R.id.tvTituloBorrador)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_borrador, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titulo.text = lista[position]
    }

    override fun getItemCount() = lista.size
}
