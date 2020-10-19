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
    //Todo: tempStorafe? CACHÉ? (avoid multiple requests)

    fun get(onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.get("/places", onSuccess, onError) //todo: limit count
    }

    fun getById(placeId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.get("/places/${placeId}", onSuccess, onError)
    }

    fun checkin(placeId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        if(mocked){
            var targetlocation = Location(LocationManager.GPS_PROVIDER)
            val seccion = Section("Patio", 20)
            var secciones: MutableList<Section> = arrayListOf(seccion)
            val mockedPlace = Place(1, "Siga la Vaca","Perro 123", "Bar", R.drawable.resto, targetlocation, secciones);
            return onSuccess(mockedPlace as JSONObject);
        }else{
            RequestUtils.put("/places/${placeId}/checkin", onSuccess, onError)
        }
    }

    fun checkout(placeId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.put("/placest/${placeId}/checkout", onSuccess, onError)
    }
}