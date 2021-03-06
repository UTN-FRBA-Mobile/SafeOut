package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerSectionListener
import com.utn_frba_mobile_2020_c2.safeout.models.SectionInfo
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils.modo
import com.utn_frba_mobile_2020_c2.safeout.views.OccupationDisplay
import kotlinx.android.synthetic.main.recycler_section.view.*


class SectionAdapter(private var sections:List<SectionInfo>, private val listener: RecyclerSectionListener)
    : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    //donde va el reemplazo de los textView con el texto que llega del objeto. Se llama por elemento
    // el View es al CardView entero.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(sectionInfo: SectionInfo, listener: RecyclerSectionListener) = with(itemView) {

        //    var occupation = ((sectionInfo.occupation.toDouble() / sectionInfo.capacity) * 100).toInt()

            textViewSectionName.text = sectionInfo.name
            //textViewSectionOccupation.text = occupation.toString() + '%'
            reservationDateOccupationDisplay.level = OccupationDisplay.calculateLevel(sectionInfo.occupation, sectionInfo.capacity)


            if (modo !== "SIN_RESERVA"){
                if (sectionInfo.reservations) {
                    buttonReservar.setOnClickListener {
                        listener.onClick(
                            sectionInfo,
                            adapterPosition
                        )
                    }
                } else {
                    buttonReservar.visibility = View.INVISIBLE
                }

            } else {
                buttonReservar.setOnClickListener {
                    listener.onClick(
                        sectionInfo,
                        adapterPosition
                    )
                }
                buttonReservar.text = "Ingresar"
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