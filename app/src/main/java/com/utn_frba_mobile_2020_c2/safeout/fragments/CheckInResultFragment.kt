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
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.JsonUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import org.json.JSONObject

class CheckInResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_result , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mode = requireArguments().getString(ARGUMENT_MODE)
        val success = requireArguments().getBoolean(ARGUMENT_SUCCESS)
        val placeId = requireArguments().getString(ARGUMENT_PLACE_ID)
        val sectionId = requireArguments().getString(ARGUMENT_SECTION_ID)

        setIcon(success)
        if (success){
            if (mode == "CHECKIN"){
                setTitle("¡Bienvenido!\nCheck IN Exitoso")
                GlobalUtils.modo = "CHECKOUT"
                if (GlobalUtils.modoReserva == null){GlobalUtils.modoReserva = "CHECKOUT"}

                GlobalUtils.checkedInSection = sectionId
            }else {
                GlobalUtils.modo = null
                if (GlobalUtils.modoReserva == "CHECKOUT"){GlobalUtils.modoReserva = null}

                setTitle("¡Gracias por su visita!\nCheck OUT Exitoso")
            }

            fetchPlaceInfo(placeId)
            fetchSectionInfo(placeId, sectionId)

            var retry = view!!.findViewById(R.id.buttonRetry) as Button
            retry.isClickable=false
            retry.visibility= View.INVISIBLE
        }else{
            if (mode == "CHECKIN"){
                setTitle("Check IN Fallido")
                setRegisterResult("Error al intentar realizar el checkin")
            } else {
                setTitle("Check OUT Fallido")
                setRegisterResult("Error al intentar realizar el checkout")
            }
        }

        // set retry/goback listener
        val buttonBack = view!!.findViewById(R.id.buttonRetry) as Button
        buttonBack.setOnClickListener {
            val activity = requireActivity()
            activity.supportFragmentManager.popBackStackImmediate()
        }

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
    private fun fetchSectionInfo(placeId: String?, sectionId: String?) {
        if(placeId !== null) {
            PlaceService.getSections(placeId) { sections, error ->
                if (error != null) {
                    ViewUtils.showSnackbar(view!!, error)
                } else {
                    val list =  JsonUtils.arrayToList(sections!!) {
                        SectionInfo.fromObject(it)
                    }
                    list?.find { it?.id == sectionId }?.let { setSectionInfo(it) }
                }
            }
        }
    }

    fun setSectionInfo(section: SectionInfo) {
        /*{
            "capacity": 50,
            "name": "Exterior",
            "place": "5f600c75db23bc5159a7ed39",
            "reservations": true,
            "occupation": 18,
            "id": "5fa2fb64f434715c664c5d11"
            }*/
        //setSectionName(section.name);
        setSectionOccupation("${section.name} : ${section.occupation.toString()}/${section.capacity.toString()}");
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

        setRegisterResult("${place.name}\n${place.address}")
        setOccupation("${place.occupation.toString()}/${place.capacity.toString()}");
    }

    fun getPlaceImage(category: String?) {
        if(category !== null){
            PlaceController.getImage("https://salina.nixi.icu/categories/${category}/image", {
                setPlaceImage(it)
            }, { _, message -> }
            )
        }
    }
    fun setPlaceImage(source: Bitmap?) {
        val t = view!!.findViewById<View>(R.id.placeImage) as ImageView
        t.setImageBitmap(source)
    }

    fun setTitle(text: String?) {
        val t = view!!.findViewById<View>(R.id.registerTitle) as TextView
        t.text = text
    }
    fun setIcon(success: Boolean) {
        val t = view!!.findViewById<View>(R.id.checkImage) as ImageView
        if(success){
            t.setImageResource(R.drawable.ic_check_circle_24dp)
        }else{
            t.setImageResource(R.drawable.ic_alert_circle_outline_24dp)
        }
        //"@drawable/ic_check_circle_24dp"
    }
/*    fun setScannedData(text: String?) {
        val t = view!!.findViewById<View>(R.id.scannedData) as TextView
        t.text = text
    }*/
    fun setRegisterResult(text: String?) {
        val t = view!!.findViewById<View>(R.id.textRegisterResult) as TextView
        t.text = text
    }
    fun setOccupation(text: String?) {
        val t = view!!.findViewById<View>(R.id.occupation) as TextView
        t.text = text
    }
    fun setSectionName(text: String?) {
        val t = view!!.findViewById<View>(R.id.sectionName) as TextView
        t.text = text
    }
    fun setSectionOccupation(text: String?) {
        val t = view!!.findViewById<View>(R.id.sectionOccupation) as TextView
        t.text = text
    }
    companion object {
        private const val ARGUMENT_MODE = "ARGUMENT_MODE"
        private const val ARGUMENT_SUCCESS = "ARGUMENT_SUCCESS"
        private const val ARGUMENT_PLACE_ID = "ARGUMENT_PLACE_ID"
        private const val ARGUMENT_SECTION_ID = "ARGUMENT_SECTION_ID"

        fun newInstance(mode: String? = "CHECKIN", success: Boolean, placeId:String? = "", sectionId:String? = "") : CheckInResultFragment{
            return CheckInResultFragment().apply {
                arguments = bundleOf(ARGUMENT_MODE to mode, ARGUMENT_SUCCESS to success, ARGUMENT_PLACE_ID to placeId, ARGUMENT_SECTION_ID to sectionId)
            }
        }

    }
}