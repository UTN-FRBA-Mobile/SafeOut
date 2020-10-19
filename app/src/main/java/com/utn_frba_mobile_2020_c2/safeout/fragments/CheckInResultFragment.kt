package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.fragments.CheckInResultFragment

class CheckInResultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.checkin_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val resultValue = requireArguments().getString(ARGUMENT_RESULT)
        val result = requireArguments().getBoolean(ARGUMENT_SUCCESS)
        val placeName = requireArguments().getString(ARGUMENT_PLACE_NAME)

        setTextResult(resultValue)
        setTextResultValue(if (result) "Registro Exitoso en ${placeName}" else "Error")

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
    companion object {
        private const val ARGUMENT_RESULT = "ARGUMENT_RESULT"
        private const val ARGUMENT_SUCCESS = "ARGUMENT_SUCCESS"
        private const val ARGUMENT_PLACE_NAME = "ARGUMENT_PLACE_NAME"

        fun newInstance(result: String, success: Boolean, placeName:String?) : CheckInResultFragment{
            return CheckInResultFragment().apply {
                arguments = bundleOf(ARGUMENT_RESULT to result, ARGUMENT_SUCCESS to success, ARGUMENT_PLACE_NAME to placeName)
            }
        }

    }
}