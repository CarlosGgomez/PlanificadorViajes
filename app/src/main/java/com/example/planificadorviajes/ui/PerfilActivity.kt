package com.example.planificadorviajes.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.planificadorviajes.R
import com.example.planificadorviajes.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ahora va la lógica del perfil, fuera del listener
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            binding.txtNombre.text = it.displayName
            binding.txtCorreo.text = it.email
            Picasso.get()
                .load(it.photoUrl)
                .placeholder(R.drawable.outline_boy_24)
                .into(binding.imgPerfil)
        }
    }
}
