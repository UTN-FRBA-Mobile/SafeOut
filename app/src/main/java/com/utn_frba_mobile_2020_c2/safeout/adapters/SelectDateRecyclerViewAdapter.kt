package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils
import com.utn_frba_mobile_2020_c2.safeout.views.OccupationDisplay


class SelectDateRecyclerViewAdapter(context: Context?) :
    RecyclerView.Adapter<SelectDateRecyclerViewAdapter.ViewHolder>() {
    var data: JsonArray = JsonArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onClickListener: ((JsonObject) -> Unit) = { _ -> }

    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.recycler_reservation_date, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val isDate = item.has("date")
        val date = DateUtils.dateFromString(item.get(if (isDate) "date" else "time").asString)
        val occupation = item.get("occupation").asInt
        val capacity = item.get("capacity").asInt
        holder.reservationDate.text = DateUtils.dateToString(date, if (isDate) DateUtils.Pattern.DISPLAY_DATE else DateUtils.Pattern.DISPLAY_TIME)
        holder.occupationDisplay.level = OccupationDisplay.calculateLevel(occupation, capacity)
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    fun getItem(position: Int): JsonObject {
        return data[position] as JsonObject
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val reservationDate: TextView = itemView.findViewById(R.id.textViewReservationDate)
        val occupationDisplay: OccupationDisplay = itemView.findViewById(R.id.reservationDateOccupationDisplay)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val item = getItem(adapterPosition)
            onClickListener(item)
        }
    }
}
