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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.google.gson.Gson
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.PlaceController
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.PlaceScanInfo
import com.utn_frba_mobile_2020_c2.safeout.services.CheckinService
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import java.io.Serializable

class QrScannerFragment : Fragment() {
        private lateinit var codeScanner: CodeScanner
        val MY_CAMERA_PERMISSION_REQUEST = 1111
        private var mPermissionGranted = false

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val mode = requireArguments().getString(QrScannerFragment.ARGUMENT_MODE)
            var title = R.string.scan_qr_in;
            if(mode != null) {
                if (mode == "READ") {
                    title = R.string.scan_qr_get;
                } else if (mode == "CHECKOUT") {
                    title = R.string.scan_qr_out;
                }
            }
            ViewUtils.setAppBarTitle(context!!.getString(title))
            return inflater.inflate(R.layout.qr_scanner_fragment, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val mode = requireArguments().getString(QrScannerFragment.ARGUMENT_MODE)
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

                        if(placeId != null && !placeId.isEmpty() && !placeId.isBlank()){
                            registerAction(mode, placeId, GlobalUtils.checkedInSection)
                        }else{
                            goToCheckinResultError(mode, "ID de lugar escaneado inválido")
                        }
                    }
                    catch (e: Exception) {
                        goToCheckinResultError(mode, e.toString())
                    }
                }
            }
            codeScanner.errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    goToCheckinResultError(mode, it.message + "")
                }
            }
            checkPermission();

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }

    private fun registerAction(mode: String? = "CHECKIN", placeId: String, sectionId: String?){
            if (mode == "CHECKOUT") {
                if (sectionId != null) {
                    checkout(mode, placeId, sectionId);
                }else{
                    ViewUtils.showSnackbar(view!!, "Error al intentar checkout")
                }
            }else{
                goToPlaceDetail(placeId)
            }

    }

    private fun registerAction_(mode: String?, placeId: String, sectionId: String){
            if(mode != null) {
                if (mode == "READ") {
                    goToPlaceDetail(placeId)
                } else if (mode == "CHECKOUT") {
                    CheckinService.checkOutOfSection(sectionId) { _, error ->
                        if (error != null) {
                            ViewUtils.showSnackbar(view!!, error)
                            goToCheckinResultError(mode, error)
                        } else {
                            goToCheckinResultSuccess(mode, placeId, sectionId)
                        }
                    }
                } else {
                    CheckinService.checkInToSection(sectionId) { _, error ->
                        if (error != null) {
                            ViewUtils.showSnackbar(view!!, error)
                            goToCheckinResultError(mode, error)
                        } else {
                            goToCheckinResultSuccess(mode, placeId, sectionId)
                        }
                    }
                }
            }
    }

    private fun goToPlaceDetail_(placeId: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(
            R.id.frameLayout,
            PlaceInfoFragment.newInstance(placeId),
            "PlaceDetail"
        )
        transaction?.addToBackStack("PlaceDetail")
        transaction?.commit()
    }

    private fun goToPlaceDetail(placeId: String) {
        PlaceService.getPlaceInfo(placeId) { placeInfo, error ->
            if (error == null) {
                val place = Gson().fromJson(placeInfo.toString(), Place::class.java)
                val lugarElegido: Serializable // Creo objeto serializable para asignarle los datos del objeto tipo Place
                lugarElegido = place
                val bundle = Bundle()
                bundle.putSerializable("lugar", lugarElegido)
                GlobalUtils.modo = "SIN_RESERVA"

                //val transaction = activity?.supportFragmentManager?.beginTransaction()
                val placeElegido = PlaceDetailFragment()
                placeElegido.arguments = bundle
                //transaction?.replace(R.id.frameLayout, placeElegido)
                //transaction?.commit()
                ViewUtils.pushFragment(this, placeElegido)

            } else {
                ViewUtils.showSnackbar(view!!, error)
                goToCheckinResultError(null, error)
            }
        }
    }

    private fun checkout(mode: String?, placeId: String, sectionId: String) {
        PlaceService.getPlaceInfo(placeId) { placeInfo, error ->
            if (error == null) {
                CheckinService.checkOutOfSection(sectionId) { _, error ->
                    if (error != null) {
                        ViewUtils.showSnackbar(view!!, error)
                        goToCheckinResultError(mode, error)
                    } else {
                        goToCheckinResultSuccess(mode, placeId, sectionId)
                    }
                }

            } else {
                ViewUtils.showSnackbar(view!!, error)
            }
        }
    }

    private fun goToCheckinResultSuccess(
        mode: String? = "CHECKIN",
        placeId: String,
        sectionId: String
    ) {
        //Toast.makeText(activity, "scanned placeinfo: ${placeId}, ${sectionId}", Toast.LENGTH_LONG).show()
        if ( mode != "READ")  GlobalUtils.checkedInSection = if ( mode == "CHECKIN") sectionId else null
        /*val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(
            R.id.frameLayout, CheckInResultFragment.newInstance(
                mode,
                true,
                placeId,
                sectionId
            ), "CheckInResult"
        )
        transaction?.addToBackStack("CheckInResult")
        transaction?.commit()*/
        ViewUtils.pushFragment(this, CheckInResultFragment.newInstance(
            mode,
            true,
            placeId,
            sectionId
        ))
    }
    private fun goToCheckinResultError(mode: String? = "CHECKIN", error: String) {
        //Toast.makeText(activity, "Error al leer QR: ${scannedData} ${error}", Toast.LENGTH_LONG).show()
        /*val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(
            R.id.frameLayout, CheckInResultFragment.newInstance(
                mode,
                false,
                "",
                ""
            ), "CheckInResult"
        )
        transaction?.addToBackStack("CheckInResult")
        transaction?.commit()*/
        ViewUtils.pushFragment(this, CheckInResultFragment.newInstance(
            mode,
            false,
            "",
            ""
        ))
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

    companion object {
        private const val ARGUMENT_MODE = "ARGUMENT_MODE"

        fun newInstance(mode: String? = "CHECKIN") : QrScannerFragment{
            return QrScannerFragment().apply {
                arguments = bundleOf(ARGUMENT_MODE to mode)
            }
        }

    }
    }
