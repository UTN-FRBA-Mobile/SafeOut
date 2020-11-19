package com.utn_frba_mobile_2020_c2.safeout.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.google.gson.Gson
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.models.PlaceScanInfo
import com.utn_frba_mobile_2020_c2.safeout.services.CheckinService
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import java.lang.Exception

class QrScannerFragment : Fragment() {
        private lateinit var codeScanner: CodeScanner
        val MY_CAMERA_PERMISSION_REQUEST = 1111
        private var mPermissionGranted = false

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.qr_scanner_fragment, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
            val activity = requireActivity()
            PlaceController.init(activity)
            codeScanner = CodeScanner(activity, scannerView)

            codeScanner.decodeCallback = DecodeCallback {
                activity.runOnUiThread {
                    try {
                        val gson = Gson()
                        var placeinfo = gson.fromJson(it.text, PlaceScanInfo::class.java)
                        val placeId = placeinfo.placeId
                        val sectionId = placeinfo.sectionId
                        //Toast.makeText(activity, "QR válido: ${it.text}", Toast.LENGTH_LONG).show()

                        CheckinService.checkInToSection(sectionId) { _, error ->
                            if (error != null) {
                                ViewUtils.showSnackbar(view!!, error)
                                goToCheckinResultError(it.text, error)
                            } else {
                                goToCheckinResultSuccess(it.text, placeId, sectionId)
                            }
                        }
                    }
                    catch (e: Exception) {
                        goToCheckinResultError(it.text, e.toString())
                    }
                }
            }
            codeScanner.errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    goToCheckinResultError("", it.message + "")
                }
            }
            checkPermission();

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }

    private fun goToCheckinResultSuccess(scannedData: String, placeId: String, sectionId: String) {
        //Toast.makeText(activity, "scanned placeinfo: ${placeId}, ${sectionId}", Toast.LENGTH_LONG).show()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout, CheckInResultFragment.newInstance(scannedData, true, placeId, sectionId), "CheckInResult")
        transaction?.addToBackStack("CheckInResult")
        transaction?.commit()
    }
    private fun goToCheckinResultError(scannedData: String, error: String) {
        //Toast.makeText(activity, "Error al leer QR: ${scannedData} ${error}", Toast.LENGTH_LONG).show()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout, CheckInResultFragment.newInstance("${scannedData} ${error}", false, "", ""), "CheckInResult")
        transaction?.addToBackStack("CheckInResult")
        transaction?.commit()
    }

    fun checkPermission(){
        val activity = requireActivity()
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                MY_CAMERA_PERMISSION_REQUEST
            )
        } else {
            mPermissionGranted = true
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==MY_CAMERA_PERMISSION_REQUEST&&grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            mPermissionGranted = true
            codeScanner.startPreview()
        }else{
            val activity = requireActivity()
            Toast.makeText(activity, "Permiso denegado por el usuario", Toast.LENGTH_LONG).show()
        }

    }

        override fun onResume() {
            super.onResume()
            if (mPermissionGranted) {
                codeScanner.startPreview()
            }
        }

        override fun onPause() {
            codeScanner.releaseResources()
            super.onPause()
        }
    }
