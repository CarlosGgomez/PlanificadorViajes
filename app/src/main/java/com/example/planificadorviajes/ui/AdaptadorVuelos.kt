import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planificadorviajes.R
import com.example.planificadorviajes.databinding.ItemVueloBinding
import com.example.planificadorviajes.model.Itinerary
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale


class AdaptadorVuelos(var vuelos: List<Itinerary>) :
    RecyclerView.Adapter<AdaptadorVuelos.VueloViewHolder>() {

    inner class VueloViewHolder(val binding: ItemVueloBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VueloViewHolder {
        val binding = ItemVueloBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VueloViewHolder(binding)
    }
    // clase que representa una sola tarjeta de vuelo en el recyclerview
// usa view binding para acceder directamente a los elementos del layout item_vuelo.xml
    override fun onBindViewHolder(holder: VueloViewHolder, position: Int) {
        val vuelo = vuelos[position]
        val leg = vuelo.legs.firstOrNull()

        leg?.let {
            val origen = it.origin.city
            val destino = it.destination.city
            val salida = formatearHora(it.departure)
            val llegada = formatearHora(it.arrival)
            val duracion = "${it.durationInMinutes / 60}h ${it.durationInMinutes % 60}m"
            val escalas = if (it.stopCount == 0) "Directo" else "${it.stopCount} escala(s)"
            val precio = vuelo.price.formatted
            val carrierLogoUrl = it.carriers.marketing.firstOrNull()?.logoUrl
            val name=it.carriers.marketing.firstOrNull()?.name


            holder.binding.tvPrecio.text = precio
            holder.binding.tvDirecto.text = escalas
            holder.binding.tvRuta.text = "$origen → $destino"
            holder.binding.tvFechas.text = "$salida → $llegada"
            holder.binding.tvDuracion.text = duracion

            if (!carrierLogoUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(carrierLogoUrl)
                    .placeholder(R.drawable.ic_flight_land)
                    .into(holder.binding.imgLogoAerolinea)
            }
            holder.binding.tvNombreAerolinea.text=name
        }
    }

    override fun getItemCount(): Int = vuelos.size

    private fun formatearHora(input: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            formatter.format(parser.parse(input) ?: return input)
        } catch (e: Exception) {
            input
        }
    }
}
