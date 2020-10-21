package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.location.Location
import android.location.LocationManager
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import com.utn_frba_mobile_2020_c2.safeout.extensions.toast
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import kotlinx.android.synthetic.main.fragment_placelist.view.*
import kotlinx.android.synthetic.main.recycler_place.view.*
import java.util.*
import kotlin.collections.ArrayList

class PlaceAdapter(private var places:List<Place>, private val listener: RecyclerPlaceListener)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(), Filterable{

    private var placesFilterList: ArrayList<Place> = places as ArrayList<Place>


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
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    placesFilterList
                else

                    placesFilterList.filter {
                        it.name.toLowerCase().contains(queryString.toLowerCase()) ||
                                it.address.toLowerCase().contains(queryString.toLowerCase()) ||
                                it.category.toLowerCase().contains(queryString.toLowerCase())
                    }
                return filterResults

        }
            /*override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    print("LLLLLL")
                    placesFilterList = places as ArrayList<Place>
                } else {
                    println("PPPPP")
                    val resultList = ArrayList<Place>()
                    //placesFilterList.clear()
                    for (row in places) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            println("UUUU")
                            resultList.add(row)
                        }
                    }
                    print(resultList)
                    placesFilterList = resultList
                }
                val filterResults = FilterResults()
                println("MMMM")
                println(placesFilterList)
                println("CCCC")
                filterResults.values = placesFilterList
                print(filterResults)

                return filterResults
            }*/

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                //placesFilterList.clear()
                //placesFilterList = results.values as ArrayList<Place>

                places = results!!.values as ArrayList<Place>
                notifyDataSetChanged()

            }
    }

}


}