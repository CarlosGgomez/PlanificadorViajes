package com.example.planificadorviajes.ui

import AdaptadorVuelos
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planificadorviajes.R
import com.example.planificadorviajes.databinding.ActivityVueloGuardadoBinding
import com.example.planificadorviajes.model.VueloGuardado
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VueloGuardadoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVueloGuardadoBinding
    private lateinit var adaptador: AdaptadorVuelosGuardados
    private val listaVuelos = mutableListOf<VueloGuardado>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVueloGuardadoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adaptador = AdaptadorVuelosGuardados(listaVuelos)
        binding.recyclerViewVuelosGuardados.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewVuelosGuardados.adapter = adaptador

        obtenerVuelosGuardados()
    }
    private fun obtenerVuelosGuardados() {
        val database = FirebaseDatabase.getInstance()
        val referencia = database.getReference("vuelos_guardados")

        referencia.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaVuelos.clear()
                for (vueloSnapshot in snapshot.children) {
                    val vuelo = vueloSnapshot.getValue(VueloGuardado::class.java)
                    vuelo?.let { listaVuelos.add(it) }
                }
                adaptador.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VueloGuardadoActivity, "Error al cargar los vuelos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
