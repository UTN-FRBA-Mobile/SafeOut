package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.app.SearchManager
import androidx.appcompat.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.adapters.PlaceAdapter
import com.utn_frba_mobile_2020_c2.safeout.extensions.toast
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import kotlinx.android.synthetic.main.fragment_placelist.*
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
                var targetlocation = Location(LocationManager.GPS_PROVIDER)
                val seccion = Section("Patio", 20)
                var secciones: MutableList<Section> = arrayListOf(seccion)

                add(
                    Place(
                        1,
                        "Siga la Vaca",
                        "Perro 123",
                        "Bar",
                        R.drawable.resto,
                        targetlocation,
                        secciones
                    )
                )
                add(
                    Place(
                        2,
                        "El Club de la Milanesa",
                        "Gato 123",
                        "Universidad",
                        R.drawable.resto,
                        targetlocation,
                        secciones
                    )
                )
                add(
                    Place(
                        3,
                        "Cesto",
                        "Pikachu 123",
                        "Biblioteca",
                        R.drawable.resto,
                        targetlocation,
                        secciones
                    )
                )
                add(
                    Place(
                        4,
                        "Guerrin",
                        "Ratata 123",
                        "Bar",
                        R.drawable.resto,
                        targetlocation,
                        secciones
                    )
                )
                add(
                    Place(
                        5,
                        "El Antojo",
                        "Raichu 123",
                        "Supermercado",
                        R.drawable.resto,
                        targetlocation,
                        secciones
                    )
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        print("HHHHH")
        inflater.inflate(R.menu.main_menu, menu)

        val menuItem = menu!!.findItem(R.id.search)

        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //list.clear()
                adapter.filter.filter(newText)
                adapter.notifyDataSetChanged()
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }


    }
