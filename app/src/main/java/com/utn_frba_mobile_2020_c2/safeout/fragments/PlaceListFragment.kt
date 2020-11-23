package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.app.SearchManager
import android.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.adapters.PlaceAdapter
import com.utn_frba_mobile_2020_c2.safeout.extensions.toast
import com.utn_frba_mobile_2020_c2.safeout.listeners.PlaceCommunicator
import com.utn_frba_mobile_2020_c2.safeout.listeners.RecyclerPlaceListener
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_placelist.*
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
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

    private lateinit var communicator: PlaceCommunicator

    private fun getPlaces(): ArrayList<Place> {
        return object : ArrayList<Place>() {
            init {
                //todo
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setAppBarTitle(context!!.getString(R.string.title_search_places))
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
                communicator = activity as PlaceCommunicator
                communicator.pasarDatosLugar(place)

            }

        }))

        recycler.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)

        val menuItem = menu!!.findItem(R.id.search)
        menuItem.expandActionView()

        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = context!!.getString(R.string.search_place)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               adapter.filter.filter(newText)
                //Thread.sleep(1000L)
                //adapter.notifyDataSetChanged()
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }


    }
