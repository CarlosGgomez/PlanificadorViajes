package com.example.planificadorviajes.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planificadorviajes.api.CotizacionVuelo
import com.example.planificadorviajes.databinding.ItemVueloBinding

class AdaptadorVuelos(private val listaVuelos: List<CotizacionVuelo>) :
    RecyclerView.Adapter<AdaptadorVuelos.VueloViewHolder>() {

    inner class VueloViewHolder(val binding: ItemVueloBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VueloViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVueloBinding.inflate(inflater, parent, false)
        return VueloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VueloViewHolder, position: Int) {
        val vuelo = listaVuelos[position]
        val contenido = vuelo.contenido

        holder.binding.tvPrecio.text = contenido.precio
        holder.binding.tvDirecto.text = if (contenido.esDirecto) "Directo" else "Con escalas"
        holder.binding.tvDuracion.text = contenido.duracionViaje

        val ruta = "${contenido.vueloIda.aeropuertoOrigen.nombre} → ${contenido.vueloIda.aeropuertoDestino.nombre}"
        holder.binding.tvRuta.text = ruta

        val fechas = "${contenido.vueloIda.fechaSalida} / ${contenido.vueloVuelta.fechaSalida}"
        holder.binding.tvFechas.text = fechas
    }

    override fun getItemCount(): Int = listaVuelos.size
}
