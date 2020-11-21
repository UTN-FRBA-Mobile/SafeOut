package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerSectionListener
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import kotlinx.android.synthetic.main.recycler_section.view.*


class SectionAdapter(private var sections:List<Section>, private val listener: RecyclerSectionListener)
    : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    //donde va el reemplazo de los textView con el texto que llega del objeto. Se llama por elemento
    // el View es al CardView entero.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(section: Section, listener: RecyclerSectionListener) = with(itemView) {

            var occupation = ((section.occupation.toDouble() / section.capacity) * 100).toInt()

            textViewSectionName.text = section.name
            textViewSectionOccupation.text = occupation.toString() + '%'

            if(section.reservations) {
                buttonReservar.setOnClickListener { listener.onClick(section, adapterPosition) }
            }else {
                buttonReservar.visibility = View.INVISIBLE
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        parent.inflate(
            R.layout.recycler_section
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(sections[position], listener)

    override fun getItemCount() = sections.size

}