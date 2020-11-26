package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.adapters.SelectDateRecyclerViewAdapter
import com.utn_frba_mobile_2020_c2.safeout.services.ReservationService
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_select_date.*
import java.util.*

class SelectDateFragment : Fragment() {
    private var rvAdapter: SelectDateRecyclerViewAdapter? = null
    private var sectionId = ""
    private var isDate = true
    private var date: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arguments = ViewUtils.getArguments()!!
        this.sectionId = arguments.get("section").asString
        this.isDate = !arguments.has("date")
        if (!this.isDate) {
            this.date = DateUtils.dateFromString(arguments.get("date").asString)
        }
        ViewUtils.setAppBarTitle(context!!.getString(if (isDate) R.string.title_select_date else R.string.title_select_time))
        return inflater.inflate(R.layout.fragment_select_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewSelectDate.layoutManager = LinearLayoutManager(activity)
        val adapter = SelectDateRecyclerViewAdapter(activity)
        adapter.onClickListener = this::onItemClicked
        recyclerViewSelectDate.adapter = adapter
        this.rvAdapter = adapter
        fetchItems()
    }

    private fun fetchItems() {
        setLoading(true)
        if (isDate) {
            ReservationService.getDates(sectionId, this::updateList)
        } else {
            ReservationService.getTimes(sectionId, date!!, this::updateList)
        }
    }

    private fun updateList(items: JsonArray?, error: String?) {
        if (error != null) {
            ViewUtils.showSnackbar(view!!, error)
        } else {
            rvAdapter!!.data = items!!
        }
        setLoading(false)
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            recyclerViewSelectDate.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            recyclerViewSelectDate.visibility = View.VISIBLE
        }
    }

    private fun onItemClicked(item: JsonObject) {
        val occupation = item.get("occupation").asInt
        val capacity = item.get("capacity").asInt
        if (occupation == capacity) {
            return
        }
        val property = if (isDate) "date" else "time"
        val date = item.get(property).asString
        val arguments = JsonObject()
        arguments.addProperty(property, date)
        ViewUtils.goBack(this, arguments)
    }
}
