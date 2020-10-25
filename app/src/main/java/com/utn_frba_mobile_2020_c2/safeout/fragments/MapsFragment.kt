package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.models.ModelMaps
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils
import org.json.JSONObject

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var mapa : GoogleMap
    private lateinit var lastLocation : Location
    private var markers : MutableList<Marker> = ArrayList()

    //dummy
    private var resutText: TextView? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mapa = googleMap
        fusedLocationProviderClient = context?.let { LocationServices.getFusedLocationProviderClient(
            it
        ) }!!

        //definir los botones de la interfaz
        mapa.uiSettings.isMyLocationButtonEnabled = true
        mapa.uiSettings.isZoomControlsEnabled = true

        setUpMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setUpMap() {
        if( context?.let { ActivityCompat.checkSelfPermission(
                it,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) } != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        //TODO: Ver que se hara si no otorga permisos para localizacion
        mapa.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if(location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                //Una vez cargado la localizacion cargo los lugares
                loadPlaces()
            }
        }

        //TODO: en proceso
        mapa.setOnCameraIdleListener(OnCameraIdleListener {
            @Override
            fun onCameraIdle() {
                loadPlaces()
            }
            onCameraIdle()
        })
    }

    private fun loadPlaces(){
        // pedir las ubicaciones y mapearlas a los puntos
        val bounds = mapa.projection.visibleRegion.latLngBounds
        val body = mapOf<String, Any>("bounds" to bounds)
        RequestUtils.post("/places/locate", body, { response ->
            showData(response)
        }, { status, error ->
            println(status)
            println(error)
        })
//        for (i in 1..10) {
//            val ranlat = Random.nextDouble()/50
//            val ranlng = Random.nextDouble()/50
//
//            val newLocation = LatLng(baseLat - ranlat, baseLng + ranlng)
//            mapa.addMarker(MarkerOptions().position(newLocation).title("Marker $i"))
//        }
        //mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.latitude, lastLocation.longitude), 15f))

    }

    private fun showData(obj: JSONObject){
        val lista = Gson().fromJson(obj.toString(), ModelMaps.Places::class.java).places
        lista.iterator().forEach { data ->
            run {
                activity?.runOnUiThread(Runnable {
                    newMarker(data)
                })
            }
        }
    }

    private fun newMarker(place: ModelMaps.Place){
        val newLocation = LatLng(
            place.location.coordinates[0].toDouble(),
            place.location.coordinates[1].toDouble()
        )
       markers.add(
           mapa.addMarker(
               MarkerOptions()
                   .position(newLocation)
                   .title(place.name)
           )
       )
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

}