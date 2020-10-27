package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.location.Location
import android.location.LocationManager
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import kotlinx.android.synthetic.main.recycler_place.view.*
import org.json.JSONArray
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
                                    var sections: JSONArray = it.getJSONObject(i)["sections"] as JSONArray
                                    var sectionArrayList: ArrayList<Section> =  arrayListOf<Section>()

                                    for (u in 0 until sections.length()){
                                        sectionArrayList.add(Section(sections.getJSONObject(u)["name"] as String,
                                            sections.getJSONObject(u)["capacity"] as Int,
                                            sections.getJSONObject(u)["occupation"] as Int
                                        ))
                                    }

                                    placesFilterList.add(
                                    Place(
                                        it.getJSONObject(i)["id"] as String,
                                        it.getJSONObject(i)["name"] as String,
                                        it.getJSONObject(i)["address"] as String,
                                        it.getJSONObject(i)["category"] as String,
                                        R.drawable.resto,
                                        targetlocation,
                                        sectionArrayList
                                    )
                                )
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