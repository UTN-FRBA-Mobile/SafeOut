package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.models.Reservation
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils

class ReservationsRecyclerViewAdapter(context: Context?) :
    RecyclerView.Adapter<ReservationsRecyclerViewAdapter.ViewHolder>() {
    var data: List<Reservation> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mCancelReservationHandler: ((Reservation, Int) -> Unit) = {_, _ -> }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.recycler_reservation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = data[position]
        holder.sectionTextView.text = "Sector ${reservation.section.name} de ${reservation.section.place.name}"
        holder.addressTextView.text = reservation.section.place.address
        holder.dateTextView.text = DateUtils.dateToString(reservation.date, DateUtils.DISPLAY)
        holder.cancelReservationButton.setOnClickListener {
            mCancelReservationHandler(reservation, position)
        }
    }

    fun setCancelReservationHandler(handler: ((Reservation, Int) -> Unit)) {
        this.mCancelReservationHandler = handler
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionTextView: TextView = itemView.findViewById(R.id.textViewMyReservationSection)
        val addressTextView: TextView = itemView.findViewById(R.id.textViewMyReservationAddress)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewMyReservationDate)
        val cancelReservationButton: Button = itemView.findViewById(R.id.buttonMyReservationCancelReservation)
    }
}
