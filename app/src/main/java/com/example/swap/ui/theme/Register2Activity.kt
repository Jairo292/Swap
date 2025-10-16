package com.example.swap.ui.theme


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView
import com.example.swap.ui.theme.IndexActivity
import com.example.swap.R


class Register2Activity : AppCompatActivity() {

    private lateinit var imgAvatar: CircleImageView
    private lateinit var tvEditAvatar: TextView
    private lateinit var etAlias: EditText
    private lateinit var btnFinish: Button

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        imgAvatar = findViewById(R.id.imgAvatar)
        tvEditAvatar = findViewById(R.id.tvEditAvatar)
        etAlias = findViewById(R.id.etAlias)
        btnFinish = findViewById(R.id.btnFinish)

        // 📸 Abrir galería al presionar el botón "Editar"
        tvEditAvatar.setOnClickListener {
            openGallery()
        }

        // 📸 También abrir galería si toca directamente el avatar
        imgAvatar.setOnClickListener {
            openGallery()
        }

        // 🚀 Botón para finalizar registro y abrir la pantalla principal
        btnFinish.setOnClickListener {
            val alias = etAlias.text.toString().trim()
            if (alias.isNotEmpty()) {
                val intent = Intent(this, IndexActivity::class.java)
                startActivity(intent)
                finish() // Cierra la pantalla de registro
            } else {
                etAlias.error = "Ingresa un alias"
            }
        }
    }

    // 🖼️ Función para abrir la galería
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // 🧩 Recibe la imagen seleccionada y la muestra en el avatar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imgAvatar.setImageURI(imageUri)
        }
    }
}
