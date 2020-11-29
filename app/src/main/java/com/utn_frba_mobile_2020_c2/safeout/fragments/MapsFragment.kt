package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.GsonBuilder
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.dto.Bounds
import com.utn_frba_mobile_2020_c2.safeout.models.ModelMaps
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_add_reservation.*
import org.json.JSONArray


class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var mapa : GoogleMap
    private lateinit var lastLocation : Location
    private var markers : MutableList<Marker> = ArrayList()
    private val gson = GsonBuilder().create()

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
        ViewUtils.setAppBarTitle(context!!.getString(R.string.title_map))
        ViewUtils.setCheckedNavItem(R.id.drawerItemMap)
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

        mapa.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            @Override
            fun onMarkerClick(marker: Marker): Boolean {
                var m = markers.find { e -> e.id == marker.id }!!.title
                var fragment = childFragmentManager
                fragment.popBackStack("Maps_Fragment", 1)
                activity!!.supportFragmentManager.beginTransaction().addToBackStack("MarkDetailFragment").commit()
                MarkDetailFragment.newInstance(m).show(childFragmentManager, "MarkDetailFragment")
                return true
            }
            onMarkerClick(it)
        })
    }

    private fun loadPlaces(){
        // pedir las ubicaciones y mapearlas a los puntos
        val bounds = Bounds.toJson(mapa.projection.visibleRegion.latLngBounds)
        val body = mapOf<String, Any>("bounds" to bounds)
        RequestUtils.postArray("/places/locate", body, { response ->
            showData(response)
        }, { _, _ ->
        })
    }

    private fun showData(obj: JSONArray){
        if(obj.length() <= 0 ) return
        val gson = GsonBuilder().create()
        val lista = gson.fromJson(obj.toString(), Array<ModelMaps.Place>::class.java).toList()
        lista.iterator().forEach { data ->
            run {
                activity?.runOnUiThread(Runnable {
                    newMarker(data)
                })
            }
        }
    }

    private fun newMarker(place: ModelMaps.Place){
        val newLocation = LatLng(place.location.latitude, place.location.longitude)
        val oc = (place.occupation.toDouble() / place.capacity.toDouble())
        var icon = R.drawable.ic_ubicacion_medium
        if(oc >= 0.90){
            icon = R.drawable.ic_ubicacion_full
        }else if( oc <= 0.5){
            icon = R.drawable.ic_ubicacion_empty
        }

       markers.add(
           mapa.addMarker(
               MarkerOptions()
                   .position(newLocation)
                   .title(gson.toJson(place).toString())
                   .icon(context?.let { bitmapDescriptorFromVector(it, icon) })
           )
       )
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

}