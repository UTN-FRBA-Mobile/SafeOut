package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.arch.core.executor.DefaultTaskExecutor
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.others.toast
import com.utn_frba_mobile_2020_c2.safeout.adapters.PlaceAdapter
import kotlinx.android.synthetic.main.fragment_placelist.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [DeparturesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaceListFragment : Fragment() {
    //solo se llama cuando se usa. Para no ocupar memoria al pedo
    private val list: ArrayList<Place> by lazy { getPlaces() }
    private val layoutManager by lazy {LinearLayoutManager(context)}

    private lateinit var recycler:RecyclerView
    private lateinit var adapter: PlaceAdapter


    private fun getPlaces(): ArrayList<Place>{
        return object: ArrayList<Place>(){
            init {
                add(Place(1, "Siga la Vaca","Perro 123", 35, R.drawable.resto))
                add(Place(2, "El Club de la Milanesa","Gato 123", 15, R.drawable.resto))
                add(Place(3, "Cesto","Pikachu 123", 40, R.drawable.resto))
                add(Place(4, "Guerrin","Ratata 123", 80, R.drawable.resto))
                add(Place(5, "El Antojo","Raichu 123", 100, R.drawable.resto))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val rootView = inflater.inflate(R.layout.fragment_placelist, container, false)
        recycler = rootView.recyclerView as RecyclerView
        setRecyclerView()
        return rootView
    }

    private fun setRecyclerView(){
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = layoutManager

        adapter = (PlaceAdapter(list, object : RecyclerPlaceListener {
            override fun onClick(place: Place, position: Int) {
                activity?.toast("Let's go to ${place.name}!")

            }

        }))
        recycler.adapter = adapter
    }
    }
