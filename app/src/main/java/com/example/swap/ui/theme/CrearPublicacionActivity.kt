package com.yourpackage.app // Use your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CrearPublicacionActivity : AppCompatActivity() {

    // 1. Declare variables for the UI elements
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnPublish: Button
    private lateinit var btnBack: ImageView
    private lateinit var ivImagePicker: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ESSENTIAL STEP: Connect the Activity to the XML layout
        setContentView(R.layout.activity_crear_publicacion)

        // 2. Initialize the UI elements (find them by their IDs)
        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        btnPublish = findViewById(R.id.btn_publish)
        btnBack = findViewById(R.id.btn_back)
        ivImagePicker = findViewById(R.id.iv_image_picker)

        // 3. Set up the event listeners

        // Back button listener
        btnBack.setOnClickListener {
            // Close the current activity and return to the previous screen
            finish()
        }

        // Publish button listener
        btnPublish.setOnClickListener {
            handlePublish()
        }

        // Image picker listener (to open the gallery/camera)
        ivImagePicker.setOnClickListener {
            openImageChooser()
        }
    }

    private fun handlePublish() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()

        // Basic validation: Check if fields are empty
        if (title.isEmpty() && description.isEmpty()) {
            Toast.makeText(this, "Por favor, agrega un título o descripción.", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO:
        // 1. Get the selected image URI (needs implementation in openImageChooser and onActivityResult)
        // 2. Upload image and post data to your backend/database.
        // 3. Navigate back to the main feed after success.

        // Placeholder for successful post simulation
        Toast.makeText(this, "Publicación creada: $title", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun openImageChooser() {
        // Implementation to open the device's image gallery
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        // Start the activity expecting a result (the chosen image URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Define a constant for the image picker request
    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}