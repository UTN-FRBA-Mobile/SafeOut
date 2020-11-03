package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.graphics.set
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import kotlinx.android.synthetic.main.recycler_place.view.*
import kotlin.collections.ArrayList

class PlaceAdapter(private var places:List<Place>, private val listener: RecyclerPlaceListener)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(), Filterable{

    private var placesFilterList: ArrayList<Place> = arrayListOf()
    private var placeImage: Bitmap? = null

    //donde va el reemplazo de los textView con el texto que llega del objeto. Se llama por elemento
    // el View es al CardView entero.
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){


        fun bind(place: Place, listener: RecyclerPlaceListener) = with(itemView){
            textViewName.text = place.name
            textViewAddress.text = place.address
            textViewCategory.text = place.category
            imageViewBackground.setImageBitmap(place.imgResource)
            //textViewOcupation.text = "${place.occupation.toString()}%"
//

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

                if  (queryString==null || queryString.isEmpty()){
                    filterResults.values = arrayListOf<Place>()
                }
                else
                {

                    if (queryString != null && queryString.length > 2) {

                        PlaceController.getImage("https://as01.epimg.net/argentina/imagenes/2020/08/29/actualidad/1598712561_382914_1598712638_noticia_normal_recorte1.jpg"
                        ,{
                                placeImage = it

                                PlaceController.search(queryString, {

                                    for (i in 0 until it.length()) {

                                        val JSONObject = it.getJSONObject(i)
                                        //JSONObject.put("imgResource", placeImage)
                                        val place = Gson().fromJson<Place>(JSONObject.toString(), Place::class.java)
                                        place.imgResource = placeImage
                                        placesFilterList.add(place)
                                    }

                                }, { _, message ->
                                    if (message != null) {
                                        //todo toast
                                    }

                                })

                            },{ _, message ->
                                if (message != null) {
                                    //todo toast
                                }})

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