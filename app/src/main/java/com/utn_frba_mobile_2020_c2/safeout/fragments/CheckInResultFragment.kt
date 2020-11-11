package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.fragments.CheckInResultFragment
import kotlinx.android.synthetic.main.recycler_place.view.*

class CheckInResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.checkin_result_fragment, container, false)
        return inflater.inflate(R.layout.fragment_register_result , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val resultValue = requireArguments().getString(ARGUMENT_RESULT)
        val result = requireArguments().getBoolean(ARGUMENT_SUCCESS)
        val placeName = requireArguments().getString(ARGUMENT_PLACE_NAME)
        val placeSection = requireArguments().getString(ARGUMENT_PLACE_SECTION)

        setTextResult(resultValue)
        //setTextResultValue(if (result) "Registro Exitoso en ${placeName}, Sección ${placeSection}" else "Error")
        setTextResultValue(if (result) "${placeName} /  ${placeSection}" else "Error")


        PlaceController.getImage("https://salina.nixi.icu/categories/Restorán/image"
            ,{
                setPlaceImage(it)
            }
            ,{_, message ->
                if (message != null) {
                    //todo toast
                }}
        )


        // set goback listener
        val buttonBack = view!!.findViewById(R.id.buttonBack) as Button
        buttonBack.setOnClickListener {
            val activity = requireActivity()
            activity.supportFragmentManager.popBackStackImmediate()
        }

    }

    fun setTextResult(text: String?) {
        val t = view!!.findViewById<View>(R.id.textResult) as TextView
        t.text = text
    }
    fun setTextResultValue(text: String?) {
        val t = view!!.findViewById<View>(R.id.textResultValue) as TextView
        t.text = text
    }
    fun setPlaceImage(source: Bitmap?) {
        val t = view!!.findViewById<View>(R.id.placeImage) as ImageView
        t.setImageBitmap(source)
    }
    companion object {
        private const val ARGUMENT_RESULT = "ARGUMENT_RESULT"
        private const val ARGUMENT_SUCCESS = "ARGUMENT_SUCCESS"
        private const val ARGUMENT_PLACE_NAME = "ARGUMENT_PLACE_NAME"
        private const val ARGUMENT_PLACE_SECTION = "ARGUMENT_PLACE_SECTION"
        //todo: change to parcelable object or extract from some temp storage?

        fun newInstance(result: String, success: Boolean, placeName:String? = "", placeSection:String? = "") : CheckInResultFragment{
            return CheckInResultFragment().apply {
                arguments = bundleOf(ARGUMENT_RESULT to result, ARGUMENT_SUCCESS to success, ARGUMENT_PLACE_NAME to placeName, ARGUMENT_PLACE_SECTION to placeSection)
            }
        }

    }
}