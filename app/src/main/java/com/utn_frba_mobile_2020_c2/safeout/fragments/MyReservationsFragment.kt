package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.adapters.ReservationsRecyclerViewAdapter
import com.utn_frba_mobile_2020_c2.safeout.models.Reservation
import com.utn_frba_mobile_2020_c2.safeout.services.ReservationService
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.JsonUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_my_reservations.*
import kotlinx.android.synthetic.main.fragment_my_reservations.view.*

class MyReservationsFragment : Fragment() {
    private var rvAdapter: ReservationsRecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        GlobalUtils.drawerActivity!!.setTitle(context!!.getString(R.string.title_my_reservations))
        val view = inflater.inflate(R.layout.fragment_my_reservations, container, false)
        view.buttonAddReservation.setOnClickListener {
            ViewUtils.pushFragment(this, AddReservationFragment())
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewReservations.layoutManager = LinearLayoutManager(activity)
        val adapter = ReservationsRecyclerViewAdapter(activity)
        adapter.setCancelReservationHandler(this::onCancelReservation)
        recyclerViewReservations.adapter = adapter
        this.rvAdapter = adapter
        fetchReservations()
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            viewContainer.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            viewContainer.visibility = View.VISIBLE
        }
    }

    private fun setReservations(reservations: List<Reservation>) {
        rvAdapter!!.data = reservations
        if (rvAdapter!!.itemCount === 0) {
            recyclerViewReservations.visibility = View.GONE
            textViewNoReservations.visibility = View.VISIBLE
        } else {
            textViewNoReservations.visibility = View.GONE
            recyclerViewReservations.visibility = View.VISIBLE
        }
    }

    private fun fetchReservations() {
        setLoading(true)
        ReservationService.getReservations { reservations, error ->
            if (error != null) {
                ViewUtils.showSnackbar(view!!, error)
            } else {
                val list =  JsonUtils.arrayToList(reservations!!) {
                    Reservation.fromObject(it)
                }
                setReservations(list)
            }
            setLoading(false)
        }
    }

    private fun onCancelReservation(reservation: Reservation, position: Int) {
        ViewUtils.showDialog(context!!, context!!.getString(R.string.dialog_cancel_reservation), positiveAction = {
            ReservationService.cancelReservation(reservation.id) { _, error ->
                if (error != null) {
                    ViewUtils.showSnackbar(view!!, error)
                } else {
                    ViewUtils.showSnackbar(view!!, context!!.getString(R.string.snackbar_reservation_canceled))
                    val reservations = ArrayList<Reservation>(rvAdapter!!.data)
                    reservations.removeAt(position)
                    setReservations(reservations)
                }
            }
        })
    }
}
