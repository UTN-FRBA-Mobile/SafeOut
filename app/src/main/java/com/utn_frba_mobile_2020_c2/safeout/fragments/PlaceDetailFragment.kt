package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.adapters.SectionAdapter
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.extensions.toast
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerSectionListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import kotlinx.android.synthetic.main.fragment_placedetail.view.*
import kotlinx.android.synthetic.main.fragment_placedetail.view.imageViewBackground
import kotlinx.android.synthetic.main.fragment_placedetail.view.textViewAddress
import kotlinx.android.synthetic.main.fragment_placedetail.view.textViewName
import kotlinx.android.synthetic.main.recycler_section.view.*
import java.io.Serializable
import java.lang.Thread.sleep

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

    private val layoutManager by lazy { LinearLayoutManager(context) }
    private var sectionList: ArrayList<Section> = arrayListOf()

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
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_placedetail, container, false)
        recycler = view.recyclerViewSections as RecyclerView

        var objetoDetalle = this.arguments
        var lugarElegido : Place
        var detalle : Serializable?

        detalle = objetoDetalle?.getSerializable("lugar")
        lugarElegido = detalle as Place

        getSections(lugarElegido.id!!)

        view.textViewAddress.text = lugarElegido.address.toString()
        view.textViewName.text    = lugarElegido.name.toString()
        view.textViewCat.text = lugarElegido.category.toString()
        view.imageViewBackground.setImageBitmap(lugarElegido.imgResource)

        return view
    }

    private fun setRecyclerView(){
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = layoutManager

        adapter = (SectionAdapter(sectionList, object : RecyclerSectionListener {
            override fun onClick(section: Section, position: Int) {
                activity?.toast("Let's go to ${section.name}!")

            }

        }))

        recycler.adapter = adapter

    }

    private fun getSections(placeId: String): ArrayList<Section> {
        return object : ArrayList<Section>() {
            init {
                PlaceController.getSections(
                    placeId
                    ,{

                        for (i in 0 until it.length()) {

                            val JSONObject = it.getJSONObject(i)
                            val section = Gson().fromJson<Section>(JSONObject.toString(), Section::class.java)

                            sectionList.add(section)

                        }

                        setRecyclerView()

                    }
                    ,{_, message ->
                        if (message != null) {
                            //todo toast
                        }}
                )
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