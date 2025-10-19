package com.example.swap.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swap.R

class NotificacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        val recyclerView = findViewById<RecyclerView>(R.id.rvNotificaciones)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val notificaciones = listOf(
            Notificacion("Jairo-FCFM le gustó tu publicación.", R.drawable.sample_post1, R.drawable.sample_post2),
            Notificacion("ADRIANA - COORDI le gustó tu publicación.", R.drawable.sample_post3, R.drawable.sample_post4),
            Notificacion("Mariana comentó: 'Me encanta esta foto!'", R.drawable.sample_post5, R.drawable.sample_post6)
        )

        recyclerView.adapter = NotificacionesAdapter(notificaciones)
    }
}

data class Notificacion(val texto: String, val perfil: Int, val imagen: Int)


class NotificacionesAdapter(private val lista: List<Notificacion>) :
    RecyclerView.Adapter<NotificacionesAdapter.ViewHolder>() {

    inner class ViewHolder(val view: android.view.View) : RecyclerView.ViewHolder(view) {
        val texto = view.findViewById<android.widget.TextView>(R.id.tvTextoNotificacion)
        val perfil = view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.imgPerfilNotif)
        val imagen = view.findViewById<android.widget.ImageView>(R.id.imgPreviewNotif)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notif = lista[position]
        holder.texto.text = notif.texto
        holder.perfil.setImageResource(notif.perfil)
        holder.imagen.setImageResource(notif.imagen)
    }

    override fun getItemCount() = lista.size
}

val ivBack = findViewById<ImageView>(R.id.ivBackNotificaciones)
ivBack.setOnClickListener {
    finish() // Cierra la actividad y vuelve atrás
}
