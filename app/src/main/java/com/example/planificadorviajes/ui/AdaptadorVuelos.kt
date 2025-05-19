package com.example.planificadorviajes.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planificadorviajes.api.CotizacionVuelo
import com.example.planificadorviajes.databinding.ItemVueloBinding

// adaptador que conecta la lista de vuelos con el recyclerview para mostrar cada tarjeta de vuelo
// recibe una lista de objetos CotizacionVuelo, que contienen toda la información necesaria para cada ítem
class AdaptadorVuelos(private val listaVuelos: List<CotizacionVuelo>) : RecyclerView.Adapter<VueloViewHolder>() {

  //este metodo se encarga de crear una nueva tarjeta de vuelo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VueloViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVueloBinding.inflate(inflater, parent, false)
        return VueloViewHolder(binding)
    }

    // este metodo se encarga de asignar los datos del vuelo

    override fun onBindViewHolder(holder: VueloViewHolder, position: Int) {
        // obtenemos el objeto CotizacionVuelo en la posición actual
        val vuelo = listaVuelos[position]
        val contenido = vuelo.contenido

        //asignamos los datos a sus etiquetas
        holder.binding.tvPrecio.text = contenido.precio
        holder.binding.tvDirecto.text = if (contenido.esDirecto) "Directo" else "Con escalas"
        holder.binding.tvDuracion.text = contenido.duracionViaje

        // construimos un texto con el nombre del aeropuerto de origen y destino del vuelo de ida
        val ruta = "${contenido.vueloIda.aeropuertoOrigen.nombre} → ${contenido.vueloIda.aeropuertoDestino.nombre}"
        holder.binding.tvRuta.text = ruta

        // mostramos las fechas de salida del vuelo de ida y del vuelo de vuelta, separadas por una barra
        val fechas = "${contenido.vueloIda.fechaSalida} / ${contenido.vueloVuelta.fechaSalida}"
        holder.binding.tvFechas.text = fechas
    }

    // devuelve el número total de vuelos que hay en la lista, lo que determina cuántas tarjetas se muestran
    override fun getItemCount(): Int = listaVuelos.size
}
