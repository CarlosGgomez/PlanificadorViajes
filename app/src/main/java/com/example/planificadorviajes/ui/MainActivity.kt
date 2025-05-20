package com.example.planificadorviajes.ui

import AdaptadorVuelos
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
                    startActivity(Intent(this, VuelosBaratosActivity::class.java))
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

    private suspend fun obtenerCodigosCiudad(ciudad: String): Pair<String, String>? {
        val respuesta = RetrofitClient.apiService.buscarAeropuerto(ciudad)
        if (respuesta.isSuccessful) {
            val lista = respuesta.body()?.data
            lista?.forEach { aeropuerto ->
                val titulo = aeropuerto.presentation?.suggestionTitle ?: return@forEach
                val entityId = aeropuerto.navigation?.entityId ?: return@forEach
                val match = Regex("\\((\\w{3})\\)").find(titulo)
                val codigoIATA = match?.groupValues?.get(1)
                if (codigoIATA != null) {
                    Log.d("CIUDAD_API", "Usando $codigoIATA y $entityId")
                    return Pair(codigoIATA, entityId)
                }
            }
        }
        return null
    }




    private fun buscarVuelos(origen: String, destino: String, fecha: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val codigosOrigen = obtenerCodigosCiudad(origen)
            val codigosDestino = obtenerCodigosCiudad(destino)

            if (codigosOrigen == null || codigosDestino == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "No se encontraron datos para alguna ciudad ingresada.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            try {
                val respuesta = RetrofitClient.apiService.buscarVuelos(
                    originSkyId = codigosOrigen.first,
                    destinationSkyId = codigosDestino.first,
                    originEntityId = codigosOrigen.second,
                    destinationEntityId = codigosDestino.second,
                    date = fecha
                )

                withContext(Dispatchers.Main) {
                    if (respuesta.isSuccessful) {
                        val vuelos = respuesta.body()?.data?.itineraries ?: emptyList()
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
                            "Error al buscar vuelos.",
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