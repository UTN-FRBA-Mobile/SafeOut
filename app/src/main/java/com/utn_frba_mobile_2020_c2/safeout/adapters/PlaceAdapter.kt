package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.location.Location
import android.location.LocationManager
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import kotlinx.android.synthetic.main.recycler_place.view.*
import org.json.JSONArray
import java.io.StringReader
import kotlin.collections.ArrayList

class PlaceAdapter(private var places:List<Place>, private val listener: RecyclerPlaceListener)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(), Filterable{

    private var placesFilterList: ArrayList<Place> = arrayListOf()


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

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                var targetlocation = Location(LocationManager.GPS_PROVIDER)
                //val seccion = Section("Patio", 20)
                //var secciones: MutableList<Section> = arrayListOf(seccion)

                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()

                if  (queryString==null || queryString.isEmpty()){
                    filterResults.values = arrayListOf<Place>()
                }
                else
                {

                    if (queryString != null && queryString.length > 2) {
                        PlaceController.search(queryString, {

                                for (i in 0 until it.length()) {

                                    val JSONObject = it.getJSONObject(i)
                                    JSONObject.put("imgResource", R.drawable.resto)
                                    val place = Gson().fromJson<Place>(JSONObject.toString(), Place::class.java)
                                    placesFilterList.add(place)
                            }

                        }, { _, message ->
                            if (message != null) {
                                //todo toast
                            }

                        })
                        filterResults.values = placesFilterList
                        placesFilterList.clear()
                    }

                }

                return filterResults

        }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                places = emptyList()

                    if (results != null) {
                        if (results.values != null) {
                            places = results!!.values as ArrayList<Place>
                        }
                }

                notifyDataSetChanged()

            }
    }

}


}