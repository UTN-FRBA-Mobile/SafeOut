package com.utn_frba_mobile_2020_c2.safeout.services

import android.location.Location
import android.location.LocationManager
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils
import org.json.JSONArray
import org.json.JSONObject


val mocked = true;

object PlaceService {
    //Todo: tempStorage? CACHÉ? (avoid multiple requests)

    fun checkin(placeId: Int, sectionId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        if(mocked){

            var targetlocation = Location(LocationManager.GPS_PROVIDER)
            val section = Section(1, "Patio", 20, 30)
            var sections: MutableList<Section> = arrayListOf(section)
            val mockedPlace = Place("1", "Siga la Vaca","Perro 123", "Bar", R.drawable.resto, targetlocation, sections);
            return onSuccess(mockedPlace as JSONObject);
        }else{
            RequestUtils.put("/places/${placeId}/sections/${sectionId}/checkin", onSuccess, onError)
        }
    }

    fun checkout(placeId: Int, sectionId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.put("/places/${placeId}/sections/${sectionId}/checkout", onSuccess, onError)
    }

    fun search(
        query: String,  onSuccess: (JSONArray) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.postPlaces("/places/search"
            ,mapOf(
                "query" to query
/*                ,
            "skip" to 0,
            "limit" to 0*/
            )
            ,  onSuccess, onError)

    }
}