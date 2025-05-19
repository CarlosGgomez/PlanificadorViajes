package com.example.planificadorviajes.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.planificadorviajes.databinding.ItemVueloBinding

// clase que representa una sola tarjeta de vuelo en el recyclerview
// usa view binding para acceder directamente a los elementos del layout item_vuelo.xml
class VueloViewHolder(val binding: ItemVueloBinding) : RecyclerView.ViewHolder(binding.root)
