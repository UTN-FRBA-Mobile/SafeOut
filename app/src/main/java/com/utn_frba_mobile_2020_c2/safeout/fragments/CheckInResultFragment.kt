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

class CheckInResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mode = requireArguments().getString(CheckInResultFragment.ARGUMENT_MODE)
        var title = R.string.scan_qr_in;
        if(mode != null) {
            if (mode == "READ") {
                title = R.string.scan_qr_get;
            } else if (mode == "CHECKOUT") {
                title = R.string.scan_qr_out;
            }
        }
        ViewUtils.setAppBarTitle(context!!.getString(title))
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
            } else if (mode == "CHECKOUT"){
                setTitle("Check OUT Fallido")
                setRegisterResult("Error al intentar realizar el checkout")
            } else{
                setTitle("Error de lectura")
                setRegisterResult("Error al intentar obtener el detalle del lugar")
            }
        }

        // set retry/goback listener
        val buttonBack = view!!.findViewById(R.id.buttonRetry) as Button
        buttonBack.setOnClickListener {
            ViewUtils.goBack(this)
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
        //setSectionName(section.name);
        setSectionOccupation("${section.name} : ${section.occupation.toString()}/${section.capacity.toString()}");
    }
    fun setPlaceInfo(placeInfo: JsonObject?) {
        val place = Gson().fromJson(placeInfo.toString(), Place::class.java)
        //getPlaceImage(place.category);

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
        //val t = view!!.findViewById<View>(R.id.occupation) as TextView
        //t.text = text
    }
    fun setSectionName(text: String?) {
        //val t = view!!.findViewById<View>(R.id.sectionName) as TextView
        //t.text = text
    }
    fun setSectionOccupation(text: String?) {
        //val t = view!!.findViewById<View>(R.id.sectionOccupation) as TextView
        //t.text = text
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