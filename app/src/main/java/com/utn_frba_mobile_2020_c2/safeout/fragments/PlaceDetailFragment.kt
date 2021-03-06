package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.adapters.SectionAdapter
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerSectionListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import com.utn_frba_mobile_2020_c2.safeout.models.SectionInfo
import com.utn_frba_mobile_2020_c2.safeout.services.CheckinService
import com.utn_frba_mobile_2020_c2.safeout.utils.JsonUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_placedetail.view.*
import kotlinx.android.synthetic.main.fragment_placedetail.view.imageViewBackground
import kotlinx.android.synthetic.main.fragment_placedetail.view.textViewAddress
import kotlinx.android.synthetic.main.fragment_placedetail.view.textViewName
import java.io.Serializable
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils.modo

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaceDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var place: Place? = null

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SectionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewUtils.setAppBarTitle(context!!.getString(R.string.title_place_detail))

        val view = inflater.inflate(R.layout.fragment_placedetail, container, false)
        recycler = view.recyclerViewSections as RecyclerView

//        var objetoDetalle = this.arguments
//        var lugarElegido : Place
//        var detalle : Serializable?

        val arguments = ViewUtils.getArguments()!!
        val lugarElegido = Place.fromObject(arguments.get("place").asJsonObject)

//        detalle = objetoDetalle?.getSerializable("lugar")

//        lugarElegido = detalle as Place
        this.place = lugarElegido

        getSections2(lugarElegido.id!!)

        view.textViewAddress.text = lugarElegido.address.toString()
        view.textViewName.text    = lugarElegido.name.toString()
        view.textViewCat.text = lugarElegido.category.toString()

        if (lugarElegido.imgResource == null){
            PlaceController.getImage("https://salina.nixi.icu/categories/${lugarElegido.category}/image"
                ,{ bitmap ->
                    view.imageViewBackground.setImageBitmap(bitmap)
                }
                , { _, _ ->
                    ViewUtils.showAlertDialog(activity!!, getString(R.string.error_image), "Aceptar!")
                }
            )
        }else{
            view.imageViewBackground.setImageBitmap(lugarElegido.imgResource)
        }

        return view
    }

    private fun setRecyclerView(sectionList: List<SectionInfo>){
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = LinearLayoutManager(activity)

        adapter = (SectionAdapter(sectionList, object : RecyclerSectionListener {
            override fun onClick(section: SectionInfo, position: Int) {

                if (modo == "SIN_RESERVA"){
                    hacerIngreso(section)
                }else{
                    makeReservation(section)
                }
            }

        }))

        recycler.adapter = adapter

    }

    private fun makeReservation(info: SectionInfo) {
        val section = Section(info.id, info.name, info.occupation, info.capacity, this.place!!, info.reservations)
        val arguments = JsonObject()
        arguments.add("section", section.toObject())
        ViewUtils.pushFragment(this, AddReservationFragment(), arguments)
    }

    private fun hacerIngreso(info: SectionInfo){
        modo = "CHECKIN"
        CheckinService.checkInToSection(info.id) { _, error ->
            if (error != null) {
                ViewUtils.showSnackbar(view!!, error)
                goToCheckinResultError(modo, error)
            } else {
                goToCheckinResultSuccess(modo, this.place?.id!!, info.id)
            }
        }
    }

    private fun goToCheckinResultSuccess(
        mode: String? = "CHECKIN",
        placeId: String,
        sectionId: String
    ) {
        if ( mode != "READ")  GlobalUtils.checkedInSection = if ( mode == "CHECKIN") sectionId else null
        ViewUtils.pushFragment(this, CheckInResultFragment.newInstance(
            mode,
            true,
            placeId,
            sectionId
        ))
    }
    private fun goToCheckinResultError(mode: String? = "CHECKIN", error: String) {
        ViewUtils.pushFragment(this, CheckInResultFragment.newInstance(
            mode,
            false,
            "",
            ""
        ))
    }


    private fun getSections2(placeId: String) {

    PlaceService.getSections(placeId) { sections, error ->
        if (error != null) {
            ViewUtils.showSnackbar(view!!, error)
        } else {
              val sectionList =  JsonUtils.arrayToList(sections!!) {
                SectionInfo.fromObject(it)
            }

            setRecyclerView(sectionList)

        }
    }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaceDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}