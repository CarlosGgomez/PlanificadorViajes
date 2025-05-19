package com.example.planificadorviajes.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planificadorviajes.R
import com.example.planificadorviajes.api.ObjetoSky
import com.example.planificadorviajes.api.ParametrosVuelo
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
        binding.etFecha.setOnClickListener {
            mostrarSelectorFecha()
        }

        binding.btnBuscar.setOnClickListener {
            buscarVuelos()
        }

        binding.rvVuelos.layoutManager = LinearLayoutManager(this)
    }

    //_-------------------------------------------------------------------------------------------
    private suspend fun obtenerParametrosVuelo(ciudad: String): ParametrosVuelo? {
        return try {
            val respuesta = ObjetoSky.retrofit.autocompletar(ciudad)
            respuesta.sugerencias.firstOrNull()?.navegacion?.parametros
        } catch (e: Exception) {
            Log.e("AutoCompletar", "Error al obtener $ciudad: ${e.message}")
            null
        }
    }
//---------------------------------------------------------------------------------------------
    //--------------------------Datepicker------------------------------------------------

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

    private fun buscarVuelos() {
        val textoOrigen = binding.etOrigen.text.toString().trim()
        val textoDestino = binding.etDestino.text.toString().trim()
        val fecha = binding.etFecha.text.toString().trim()

        //recoge los datos que escribe el usuario y el trim quita los espacios en blanco



        // comprueba campos vacios
        if (textoOrigen.isEmpty() || textoDestino.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {

                //obtiene los parametros de origen y destino de la api
                val origen = obtenerParametrosVuelo(textoOrigen)
                val destino = obtenerParametrosVuelo(textoDestino)

                if (origen == null || destino == null) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "No se reconocieron las ubicaciones.", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                //llama al metodo buscarVuelos pasandole el skyID y el entityID y las fechas
                val respuesta = ObjetoSky.retrofit.buscarVuelos(
                    origen = origen.skyId,
                    idOrigen = origen.entityId,
                    destino = destino.skyId,
                    idDestino = destino.entityId,
                    fechaIda = fecha,
                    fechaVuelta = fecha
                )

                //accede a la lista de vuelos
                val lista = respuesta.datos.cotizaciones.resultados

                // si hay vuelos, crea el adaptador y lo asigna al recyclerview
                    if (lista.isNotEmpty()) {
                        adaptador = AdaptadorVuelos(lista)
                        binding.rvVuelos.adapter = adaptador
                    } else {
                        Toast.makeText(this@MainActivity, "No se encontraron vuelos.", Toast.LENGTH_SHORT).show()
                    }


            } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
