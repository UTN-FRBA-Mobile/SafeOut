package com.utn_frba_mobile_2020_c2.safeout.services

import android.location.Location
import android.location.LocationManager
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils
import org.json.JSONObject


val mocked = true;

object PlaceService {
    //Todo: tempStorage? CACHÉ? (avoid multiple requests)

    fun get(onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.get("/places", onSuccess, onError) //todo: limit count
    }

    //todo: sectionId?
    fun checkin(placeId: Int, section: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        if(mocked){
            var targetlocation = Location(LocationManager.GPS_PROVIDER)
            val seccion = Section("Patio", 20)
            var secciones: MutableList<Section> = arrayListOf(seccion)
            val mockedPlace = Place(1, "Siga la Vaca","Perro 123", "Bar", R.drawable.resto, targetlocation, secciones);
            return onSuccess(mockedPlace as JSONObject);
        }else{
            RequestUtils.put("/places/${placeId}/${section}/checkin", onSuccess, onError)
        }
    }

    fun checkout(placeId: Int, section: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.put("/places/${placeId}/${section}/checkout", onSuccess, onError)
    }
}