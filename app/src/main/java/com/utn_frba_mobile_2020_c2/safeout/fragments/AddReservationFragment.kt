package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.extensions.optionallyGet
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.JsonUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_add_reservation.*

class AddReservationFragment : Fragment() {
    private var section: Section? = null
    private var date: String? = null
    private var time: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewUtils.setAppBarTitle("Agregar Reserva")
        val arguments = ViewUtils.getArguments()!!
        this.section = Section.fromObject(arguments.get("section").asJsonObject)
        this.date = arguments.optionallyGet("date")?.asString
        this.time = arguments.optionallyGet("time")?.asString
        return inflater.inflate(R.layout.fragment_add_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewAddReservationPlace.text = section!!.place.name
        textViewAddReservationSection.text = section!!.name
        if (date != null) {
            textViewAddReservationDate.text = DateUtils.dateToString(DateUtils.dateFromString(date!!), DateUtils.Pattern.DISPLAY_DATE)
            reservationTimeSelector.visibility = View.VISIBLE
        }
        if (time != null) {
            textViewAddReservationTime.text = DateUtils.dateToString(DateUtils.dateFromString(time!!), DateUtils.Pattern.DISPLAY_TIME)
            buttonSubmitReservation.isEnabled = true
        }
        reservationDateSelector.setOnClickListener { goToSelectScreen(true) }
        reservationTimeSelector.setOnClickListener { goToSelectScreen(false) }
        buttonSubmitReservation.setOnClickListener(this::submitReservation)
    }

    private fun goToSelectScreen(isDate: Boolean) {
        val arguments = JsonObject()
        arguments.addProperty("section", section!!.id)
        if (!isDate) {
            arguments.addProperty("date", date!!)
        }
        ViewUtils.pushFragment(this, SelectDateFragment(), arguments)
    }

    private fun submitReservation(view: View) {
        // TODO
    }

}
