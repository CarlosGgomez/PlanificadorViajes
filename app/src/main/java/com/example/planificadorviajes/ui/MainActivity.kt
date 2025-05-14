package com.example.planificadorviajes.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planificadorviajes.api.ObjetoSky
import com.example.planificadorviajes.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptador: AdaptadorVuelos  // lo crearemos luego

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etFecha.setOnClickListener {
            mostrarSelectorFecha()
        }

        binding.btnBuscar.setOnClickListener {
            buscarVuelos()
        }

        binding.rvVuelos.layoutManager = LinearLayoutManager(this)
    }

    private fun mostrarSelectorFecha() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val selectorFecha = DatePickerDialog(this, { _, y, m, d ->
            val mesFormateado = (m + 1).toString().padStart(2, '0')
            val diaFormateado = d.toString().padStart(2, '0')
            val fecha = "$y-$mesFormateado-$diaFormateado"
            binding.etFecha.setText(fecha)
        }, anio, mes, dia)

        selectorFecha.show()
    }

    private fun buscarVuelos() {
        val origen = "NYCA"
        val idOrigen = "27537542"
        val destino = "HNL"
        val idDestino = "95673827"
        val fecha = "2025-06-02"


        if (origen.isEmpty() || destino.isEmpty() || fecha.isEmpty()) {
            Log.d("Validación", "Todos los campos son obligatorios")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val respuesta = ObjetoSky.retrofit.buscarVuelos(
                    origen = origen,
                    idOrigen = idOrigen,
                    destino = destino,
                    idDestino = idDestino,
                    fechaIda = fecha,
                    fechaVuelta = "2025-06-04"
                )
                val lista = respuesta.datos.cotizaciones.resultados

                runOnUiThread {
                    if (lista.isNotEmpty()) {
                        adaptador = AdaptadorVuelos(lista)
                        binding.rvVuelos.adapter = adaptador
                    } else {
                        Toast.makeText(this@MainActivity, "No se encontraron vuelos.", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                Log.e("ErrorAPI", "Falló la búsqueda: ${e.message}")
            }
        }

    }
}
