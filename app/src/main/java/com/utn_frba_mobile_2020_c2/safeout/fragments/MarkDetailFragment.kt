package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils2
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import com.utn_frba_mobile_2020_c2.safeout.views.OccupationDisplay
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_add_reservation.*
import kotlinx.android.synthetic.main.fragment_mark_detail.*
import java.io.Serializable


private const val ARG_PARAM1 = "param1"

class MarkDetailFragment() : DialogFragment() {

    private var param1: String? = null
    private lateinit var local: Place

    //var dialog = DialogFragment().dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

            val gson = GsonBuilder().create()
            local = gson.fromJson(param1, Place::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.fui_idp_button_background_apple);
        return inflater.inflate(R.layout.fragment_mark_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mark_address.text = local.address
        mark_name.text = local.name
        mark_category.text = local.category
        value_capacity.text = local.capacity.toString()
        value_ocupation.text = local.occupation.toString()
        occupationDisplay.level = OccupationDisplay.calculateLevel(local.occupation!!, local.capacity!!)
        button_info.setOnClickListener {
            openInfo()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        @JvmStatic
        fun newInstance(marker: String) =
            MarkDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, marker)
                }
            }
    }

    private fun openInfo(){
        local.id?.let {
            PlaceService.getPlaceInfo(it) { placeInfo, error ->
                if (error != null) {
                    ViewUtils.showSnackbar(view!!, error)
                } else {
                    var lugar : Place =  Gson().fromJson(placeInfo.toString(), Place::class.java)
                    PlaceController.getImage("https://salina.nixi.icu/categories/${lugar.category}/image"
                        ,{ bitmap ->
                            lugar.imgResource = bitmap
                            val arguments = JsonObject()
                            arguments.add("place", lugar.toObject())
                            ViewUtils.pushFragment(parentFragment!!, PlaceDetailFragment(), arguments)
                            dialog!!.cancel()
                        }
                        , { _, _ ->
                            ViewUtils.showAlertDialog(activity!!, getString(R.string.error_image), "Aceptar!")
                        }
                    )
                }
            }
        }
    }

}