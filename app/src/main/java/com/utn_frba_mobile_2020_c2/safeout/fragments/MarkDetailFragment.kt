package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.R.attr.button
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.models.ModelMaps
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import kotlinx.android.synthetic.main.fragment_mark_detail.*
import kotlinx.android.synthetic.main.fragment_mark_detail.view.*
import org.json.JSONObject
import java.util.*


private const val ARG_PARAM1 = "param1"

class MarkDetailFragment() : DialogFragment() {

    private var param1: String? = null
    private lateinit var local: Place

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
        value_ocupation

        button_info.setOnClickListener {
            openInfo()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
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
        println(local)
    }

}