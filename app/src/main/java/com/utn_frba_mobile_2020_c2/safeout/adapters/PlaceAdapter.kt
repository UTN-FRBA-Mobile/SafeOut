package com.utn_frba_mobile_2020_c2.safeout.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.graphics.set
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.extensions.inflate
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.recycler_place.view.*
import kotlin.collections.ArrayList

class PlaceAdapter(private var places:List<Place>,  private var view : View, private val listener: RecyclerPlaceListener)
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

                                PlaceController.search(queryString, { it ->

                                    for (i in 0 until it.length()) {

                                        val JSONObject = it.getJSONObject(i)
                                        //JSONObject.put("imgResource", placeImage)
                                        val place = Gson().fromJson<Place>(JSONObject.toString(), Place::class.java)
                                        place.imgResource = placeImage

                                       PlaceController.getImage("https://salina.nixi.icu/categories/${place.category}/image"
                                            ,{
                                                place.imgResource = it
                                            }
                                            ,{_, message ->
                                                if (message != null) {
                                                    ViewUtils.showSnackbar(view, message)
                                                    //todo toast
                                                }}
                                            )

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