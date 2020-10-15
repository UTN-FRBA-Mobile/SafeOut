package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
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
import com.google.android.gms.maps.model.VisibleRegion
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.models.ModelMaps
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import com.utn_frba_mobile_2020_c2.safeout.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.random.Random


class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener  {

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private var onCameraMoveListener: GoogleMap.OnCameraMoveListener? = null
    private lateinit var mapa : GoogleMap
    private lateinit var lastLocation : Location

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
        configureCameraIdle();
    }

    private fun configureCameraIdle() {
        onCameraMoveListener = GoogleMap.OnCameraMoveListener {
//            val location: Location = mapa.myLocation
            loadPlaces(lastLocation)
        }
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
                mapa.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                //Una vez cargado la localizacion cargo los lugares
                loadPlaces(location)
            }
        }

        //TODO: en proceso
        //mapa.setOnCameraMoveListener(onCameraMoveListener);
    }

    private fun loadPlaces(location: Location){
        val baseLat = location.latitude
        val baseLng = location.longitude

        // pedir las ubicaciones y mapearlas a los puntos
        val bounds = mapa.projection.visibleRegion.latLngBounds
        val service = ServiceBuilder.buildService(PlaceService::class.java)
        val response = service.getPlaces(
            bounds.northeast.latitude,
            bounds.northeast.longitude,
            bounds.southwest.latitude,
            bounds.southwest.longitude
        )

        response.enqueue(object : Callback<ModelMaps.Places> {
            override fun onResponse(
                call: Call<ModelMaps.Places>,
                response: Response<ModelMaps.Places>
            ) {
                if (response.isSuccessful) {
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<ModelMaps.Places>, t: Throwable) {
                println("fail to request")
            }
        })

        for (i in 1..10) {
            val ranlat = Random.nextDouble()/50
            val ranlng = Random.nextDouble()/50

            val newLocation = LatLng(baseLat - ranlat, baseLng + ranlng)
            mapa.addMarker(MarkerOptions().position(newLocation).title("Marker $i"))
        }

        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(baseLat, baseLng), 15f))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

}