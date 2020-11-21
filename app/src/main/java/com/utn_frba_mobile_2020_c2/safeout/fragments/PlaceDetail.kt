package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.models.*
import com.utn_frba_mobile_2020_c2.safeout.services.CheckinService
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.JsonUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import org.json.JSONObject

class PlaceDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_detail , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val placeId = requireArguments().getString(ARGUMENT_PLACE_ID)

        setTitle("Detalle de lugar")
        fetchPlaceInfo(placeId)
        fetchSectionsInfo(placeId)
    }

    private fun fetchPlaceInfo(placeId: String?) {
        if(placeId !== null) {
            PlaceService.getPlaceInfo(placeId) { placeInfo, error ->
                if (error != null) {
                    ViewUtils.showSnackbar(view!!, error)
                } else {
                    setPlaceInfo(placeInfo)
                }
            }
        }
    }
    private fun fetchSectionsInfo(placeId: String?) {
        if(placeId !== null) {
            PlaceService.getSections(placeId) { sections, error ->
                if (error != null) {
                    ViewUtils.showSnackbar(view!!, error)
                } else {
                    val list =  JsonUtils.arrayToList(sections!!) {
                        SectionInfo.fromObject(it)
                    }
                    setSectionsInfo(list)
                }
            }
        }
    }

    fun setPlaceInfo(placeInfo: JsonObject?) {
        /*{
            "address": "Republica Arabe Siria 3277",
            "category": "Restorán",
            "location":{
            "longitude": -58.4117268,
            "latitude": -34.578246
        },
            "name": "Bella Italia",
            "id": "5f600c75db23bc5159a7ed39",
            "capacity": 120,
            "occupation": 18
        }*/

        val place = Gson().fromJson(placeInfo.toString(), Place::class.java)
        getPlaceImage(place.category);
        setPlaceName(place.name);
        setPlaceAddress(place.address);
        setPlaceOccupation("${place.occupation.toString()}/${place.capacity.toString()}");
    }

    fun setSectionsInfo(sections: List<SectionInfo>) {
        /*{
            "capacity": 50,
            "name": "Exterior",
            "place": "5f600c75db23bc5159a7ed39",
            "reservations": true,
            "occupation": 18,
            "id": "5fa2fb64f434715c664c5d11"
            }*/
        //setSectionName(section.name);
    }

    fun getPlaceImage(category: String?) {
        if(category !== null){
            PlaceController.getImage("https://salina.nixi.icu/categories/${category}/image", {
                setPlaceImage(it)
            }, { _, message -> }
            )
        }
    }

    fun setTitle(text: String?) {
        val t = view!!.findViewById<View>(R.id.detailTitle) as TextView
        t.text = text
    }
    fun setPlaceImage(source: Bitmap?) {
        val t = view!!.findViewById<View>(R.id.placeImage) as ImageView
        t.setImageBitmap(source)
    }
    fun setPlaceAddress(text: String?) {
        val t = view!!.findViewById<View>(R.id.placeAddress) as TextView
        t.text = text
    }
    fun setPlaceName(text: String?) {
        val t = view!!.findViewById<View>(R.id.placeName) as TextView
        t.text = text
    }
    fun setPlaceOccupation(text: String?) {
        val t = view!!.findViewById<View>(R.id.placeOccupation) as TextView
        t.text = text
    }
    companion object {
        private const val ARGUMENT_PLACE_ID = "ARGUMENT_PLACE_ID"

        fun newInstance(placeId:String? = "", sectionId:String? = "") : PlaceDetailFragment{
            return PlaceDetailFragment().apply {
                arguments = bundleOf(ARGUMENT_PLACE_ID to placeId)
            }
        }

    }
}