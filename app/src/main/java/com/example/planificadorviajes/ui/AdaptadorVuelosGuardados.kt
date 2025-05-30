package com.example.planificadorviajes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planificadorviajes.R
import com.example.planificadorviajes.databinding.ItemVueloBinding
import com.example.planificadorviajes.model.VueloGuardado
import com.squareup.picasso.Picasso

class AdaptadorVuelosGuardados(private val vuelos: List<VueloGuardado>) :
    RecyclerView.Adapter<AdaptadorVuelosGuardados.VueloViewHolder>() {

    inner class VueloViewHolder(val binding: ItemVueloBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VueloViewHolder {
        val binding = ItemVueloBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VueloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VueloViewHolder, position: Int) {
        val vuelo = vuelos[position]
        holder.binding.tvRuta.text = "${vuelo.origen} → ${vuelo.destino}"
        holder.binding.tvFechas.text = "${vuelo.salida} → ${vuelo.llegada}"
        holder.binding.tvDuracion.text = vuelo.duracion
        holder.binding.tvDirecto.text = vuelo.escalas
        holder.binding.tvPrecio.text = vuelo.precio
        holder.binding.tvNombreAerolinea.text = vuelo.aerolinea

        Picasso.get()
            .load(vuelo.logoUrl)
            .placeholder(R.drawable.ic_flight_land)
            .into(holder.binding.imgLogoAerolinea)
    }

    override fun getItemCount(): Int = vuelos.size
}
