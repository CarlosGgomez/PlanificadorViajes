package com.example.planificadorviajes.ui

import AdaptadorVuelos
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planificadorviajes.R
import com.example.planificadorviajes.api.RetrofitClient
import com.example.planificadorviajes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptador: AdaptadorVuelos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adaptador = AdaptadorVuelos(emptyList())
        binding.rvVuelos.layoutManager = LinearLayoutManager(this)
        binding.rvVuelos.adapter = adaptador
//------------------------------------------------------------------------------------------------------

//-------------------- abre los activitys si se toca los item del menu-------------------------
        val drawerLayout = binding.drawerLayout
        val navView = binding.navigationView

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_buscar -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }

                R.id.nav_baratos -> {

                }

                R.id.nav_info -> {

                }

            }
            drawerLayout.closeDrawers()
            true
        }
//-------------------------------------------------------------------------------------------------

//-----------------------Listener de datepicker y boton buscar--------------------------------------
        binding.rvVuelos.layoutManager = LinearLayoutManager(this)

        binding.btnBuscar.setOnClickListener {
            val origen = binding.etOrigen.text.toString().trim()
            val destino = binding.etDestino.text.toString().trim()
            val fecha = binding.etFecha.text.toString().trim()

            if (origen.isEmpty() || destino.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            buscarVuelos(origen, destino, fecha)
        }
        binding.etFecha.setOnClickListener {
            mostrarSelectorFecha()
        }

    }


    //_-------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------
// --------------------------Datepicker------------------------------------------------

    private fun mostrarSelectorFecha() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        //------obtiene la fecha actual del sistema


        //crea un dialogo de selector de fecha con año mes y dia
        val selectorFecha = DatePickerDialog(this, { _, y, m, d ->


            //suma uno al mes porque enero es 0, foormatea los dias (02)

            val mesFormateado = (m + 1).toString().padStart(2, '0')
            val diaFormateado = d.toString().padStart(2, '0')


            // conviertea la fecha en un string y se lo asigna a la variable

            val fecha = "$y-$mesFormateado-$diaFormateado"
            binding.etFecha.setText(fecha)
        }, anio, mes, dia)

        selectorFecha.show()
    }

    //---------------------------------------------------------------------------------------------
    private fun obtenerEntityId(ciudad: String): String {
        return when (ciudad.lowercase()) {
            "london" -> "27544008"
            "new york" -> "27537542"
            "madrid" -> "27539733"
            "barcelona" -> "27539729"
            else -> "27544008" // fallback
        }
    }

    private fun buscarVuelos(origen: String, destino: String, fecha: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.buscarVuelos(
                    originSkyId = origen.uppercase(),
                    destinationSkyId = destino.uppercase(),
                    originEntityId = obtenerEntityId(origen),
                    destinationEntityId = obtenerEntityId(destino),
                    date = fecha
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val vuelos = response.body()?.data?.itineraries ?: emptyList()
                        if (vuelos.isNotEmpty()) {
                            adaptador.vuelos = vuelos
                            adaptador.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "No se encontraron vuelos.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error en la respuesta del servidor.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}