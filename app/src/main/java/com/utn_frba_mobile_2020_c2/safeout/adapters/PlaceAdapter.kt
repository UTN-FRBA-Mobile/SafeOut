package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import kotlinx.android.synthetic.main.recycler_place.view.*

class PlaceAdapter(private val places:List<Place>, private val listener: RecyclerPlaceListener)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    //donde va el reemplazo de los textView con el texto que llega del objeto. Se llama por elemento
    // el View es al CardView entero.
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(place: Place, listener: RecyclerPlaceListener) = with(itemView){
            textViewName.text = place.name
            textViewAddress.text = place.address
            textViewCategory.text = place.category
            //textViewOcupation.text = "${place.occupation.toString()}%"
            imageViewBackground.setImageResource(place.imgResource)

            setOnClickListener { listener.onClick(place, adapterPosition) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(
        R.layout.recycler_place))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(places[position], listener)

    override fun getItemCount() = places.size
}