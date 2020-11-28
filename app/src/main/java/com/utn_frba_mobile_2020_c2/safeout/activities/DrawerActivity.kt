package com.utn_frba_mobile_2020_c2.safeout.activities

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.AuthController
import com.utn_frba_mobile_2020_c2.safeout.fragments.*
import kotlinx.android.synthetic.main.activity_drawer.*
import com.utn_frba_mobile_2020_c2.safeout.listeners.*
import com.utn_frba_mobile_2020_c2.safeout.models.ModelMaps
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import java.io.Serializable
import com.utn_frba_mobile_2020_c2.safeout.fragments.HomeFragment
import com.utn_frba_mobile_2020_c2.safeout.fragments.MapsFragment
import com.utn_frba_mobile_2020_c2.safeout.fragments.PlaceListFragment
import com.utn_frba_mobile_2020_c2.safeout.fragments.QrScannerFragment
import com.utn_frba_mobile_2020_c2.safeout.fragments.NfcFragment
import kotlinx.android.synthetic.main.activity_drawer.*
import com.utn_frba_mobile_2020_c2.safeout.extensions.*

import com.utn_frba_mobile_2020_c2.safeout.fragments.*
import com.utn_frba_mobile_2020_c2.safeout.models.Reservation
import com.utn_frba_mobile_2020_c2.safeout.services.CheckinService
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import com.utn_frba_mobile_2020_c2.safeout.services.ReservationService
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils.modo
import com.utn_frba_mobile_2020_c2.safeout.utils.JsonUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.serialization.descriptors.PrimitiveKind

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, PlaceCommunicator {
    private var mToggle: ActionBarDrawerToggle? = null
    private var mToolBarNavigationListenerIsRegistered = false
    private var nfcPendingIntent: PendingIntent? = null
    private var nfcAdapter: NfcAdapter? = null
    private var evitarIngreso = false

    private val ID_SUBE = 1167939230587520 // Corresponde al Restaurant "Sigue al conejo blanco" "5f600c84db23bc5159a81aa4" "Republica Arabe Siria 3277"
    private val ID_CONEJO = "5f600c84db23bc5159a81aa4"
//    private val ID_ITALIA_EXTERIOR = "5fa2fb64f434715c664c5d11" // -> Con reserva

    private val ID_MASTER = 1558907772936448 // Corresponde al Restaurant "Siga la Vaca" de Monroe 1802 "5f600c7adb23bc5159a7fb8d"
    private val ID_ITALIA = "5f600c76db23bc5159a7eed4"
//    private val ID_ITALIA_INTERIOR = "5fa2fb64f434715c664c5d10"  // -> Sin reserva

    private val ID_SUBE_J: Long = 909384705
    private val ID_SUBE_J_PLACE = "5f600c75db23bc5159a7ed60" //con reserva

    private val ID_SUBE_J2: Long = 750959485
    private val ID_SUBE_J2_PLACE = "5f600c75db23bc5159a7ed47" //Sin reserva

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalUtils.drawerActivity = this
        setContentView(R.layout.activity_drawer)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle()

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        navView.setCheckedItem(R.id.drawerItemHome)
        setVisibleFragment(HomeFragment())
        AuthController.init(this)

        val loggedUserName = AuthController.loggedUser?.get("name") as String
        val headerView = navView.getHeaderView(0)
        val drawerLoggedUser = headerView.findViewById<TextView>(R.id.drawerLoggedUser)
        drawerLoggedUser.text = loggedUserName
        this.mToggle = toggle

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            val nav_Menu: Menu = navView.getMenu()
            nav_Menu.findItem(R.id.CheckinNFC).setVisible(false)
        }
        /*// TODO: How to force update of items?
        val nav_Menu: Menu = navView.getMenu()
        if(GlobalUtils.checkedInSection !== null){
            nav_Menu.findItem(R.id.drawerItemCheckIn).setVisible(false)
            nav_Menu.findItem(R.id.drawerItemCheckOut).setVisible(true)
        }else{
            nav_Menu.findItem(R.id.drawerItemCheckOut).setVisible(false)
            nav_Menu.findItem(R.id.drawerItemCheckIn).setVisible(true)
        }*/

        println("GlobalUtils.modoReserva" + GlobalUtils.modoReserva)

        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            ViewUtils.goBack()
            if (GlobalUtils.backStackSize >= 0) {
                super.onBackPressed()
            } else {
                GlobalUtils.backStackSize = 0
                moveTaskToBack(true)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawerItemHome -> {
                setVisibleFragment(HomeFragment())
            }
            R.id.drawerItemMap -> {
                setVisibleFragment(MapsFragment())
            }
            R.id.drawerItemSearch -> {
                setVisibleFragment(PlaceListFragment())
            }
            R.id.drawerItemCheckIn -> {
                val mode = if (GlobalUtils.checkedInSection !== null) "CHECKOUT" else "CHECKIN"
                setVisibleFragment(QrScannerFragment.newInstance(mode))
            }
            R.id.CheckinNFC -> {
                if (nfcAdapter != null) {
                    setVisibleFragment(NfcFragment())
                }
            }
            R.id.drawerItemLogout -> {
                AuthController.logout()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
            R.id.drawerItemMyReservations -> {
                setVisibleFragment(MyReservationsFragment())
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setVisibleFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent != null) processIntent(intent)
    }

    private fun processIntent(checkIntent: Intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED == checkIntent.action) {
            val tag = checkIntent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            var idLugar: Long = deHexadecimalAEntero(byteArrayToHexString(tag?.id))

/*          INFO EXTRA
            var techList = tag?.techList
            println("Tecnologías usadas por la tarjeta :" + tag?.techList?.component1().toString())
            tag?.id --> es un ByteArray

            println("Tecnologías usadas por la tarjeta : " + tag.toString())
            println("ID Tag leido del lugar en Decimal : " + idLugar)
            println("ID Tag leido del lugar en HEXA    : " + byteArrayToHexString(tag?.id))
 */

            var placeId: String

            when (idLugar) {
                ID_SUBE -> {
                    placeId = ID_CONEJO
                }
                ID_MASTER -> {
                    placeId = ID_ITALIA
                }
                ID_SUBE_J -> {
                    placeId = ID_SUBE_J_PLACE
                }
                ID_SUBE_J2 -> {
                    placeId = ID_SUBE_J2_PLACE
                }
                else -> {
                    placeId = idLugar.toString()
                }
            }

            var mode: String? = null

            println("GlobalUtils.modoReserva" + GlobalUtils.modoReserva)

            ReservationService.getReservations { reservations, error ->
                if(error == null) {
                    val list = JsonUtils.arrayToList(reservations!!) {
                        Reservation.fromObject(it)
                    }
                   val unaReserva = list?.find { it?.section.place.id == placeId }?.let {
                        CheckinService.checkInToSection(it?.section.id) { _, error ->
                            if (error != null) {
                                if (GlobalUtils.modoReserva == "CHECKOUT"){
                                    goToCheckinResultSuccess("CHECKOUT", placeId, it?.section.id)

                                }else {
                                    ViewUtils.showAlertDialog(
                                        this,
                                        "Para el ingreso a tu reservacion a ${it.section.place.name} aun es muy temprano.",
                                        "Entendido!"
                                    )

                                    goToCheckinResultError("CHECKIN", error)
                                }
                            } else {
                                if (GlobalUtils.modoReserva == null) {
                                    goToCheckinResultSuccess("CHECKIN", placeId, it?.section.id)
                                }
                            }
                        }
                    }

                    if (unaReserva == null){
                        if (GlobalUtils.checkedInSection == null) {
                            mode = "CHECKIN"

                            elegirSeccionSinReserva(placeId)
                        } else {
                            mode = "CHECKOUT"

                            CheckinService.checkOutOfSection(GlobalUtils.checkedInSection!!) { _, error ->
                                if (error != null) {
                                    //ViewUtils.showSnackbar(, error)
                                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()

                                    goToCheckinResultError(mode, error)
                                } else {
                                    goToCheckinResultSuccess(mode, placeId, GlobalUtils.checkedInSection!!)

                                }
                            }
                        }
                    }

                }else{
                    Toast.makeText(this, "Error al acceder a la base de reservas", Toast.LENGTH_LONG).show()
                }
            }

        } else {
            Toast.makeText(this, "Error, vuelva a intentar", Toast.LENGTH_LONG).show()
        }
    }

     private fun elegirSeccionSinReserva(placeId: String) {

        if (placeId != null){
            PlaceService.getPlaceInfo(placeId){ placeInfo, error ->
                if (error == null) {
                    val place = Gson().fromJson(placeInfo.toString(), Place::class.java)
                    val lugarElegido: Serializable // Creo objeto serializable para asignarle los datos del objeto tipo Place
                    lugarElegido = place
                    val bundle = Bundle()
                    bundle.putSerializable("lugar", lugarElegido)
                    modo = "SIN_RESERVA"

                    val transaction = this.supportFragmentManager.beginTransaction()
                    val placeElegido = PlaceDetailFragment()
                    placeElegido.arguments = bundle
                    transaction.replace(R.id.frameLayout, placeElegido)
                    transaction.commit()
                }else{
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun pasarDatosLugar(lugar: Place) {

        // Para que me tome la clase Place como Serializable al pasarlo tuve que asignarlo

        val otroLugar : Serializable // Creo objeto serializable para asignarle los datos del objeto tipo Place
        otroLugar = lugar
        val bundle = Bundle()
        bundle.putSerializable("lugar", otroLugar)

        val transaction = this.supportFragmentManager.beginTransaction()
        val placeElegido = PlaceDetailFragment()
        placeElegido.arguments = bundle
        transaction.replace(R.id.frameLayout, placeElegido)
        transaction.commit()

    }
  
    private fun goToCheckinResultSuccess(mode: String? = "CHECKIN", placeId: String, sectionId: String) {
        if ( mode != "READ")  GlobalUtils.checkedInSection = if ( mode == "CHECKIN") sectionId else null

        val transaction = supportFragmentManager?.beginTransaction()
        transaction?.replace(
            R.id.frameLayout, CheckInResultFragment.newInstance(
                mode,
                true,
                placeId,
                sectionId
            ), "CheckInResult"
        )
        transaction?.addToBackStack("CheckInResult")
        transaction?.commit()
    }

    private fun goToCheckinResultError(mode: String? = "CHECKIN", error: String) {
        val transaction = supportFragmentManager?.beginTransaction()
        transaction?.replace(
            R.id.frameLayout, CheckInResultFragment.newInstance(
                mode,
                false,
                "",
                ""
            ), "CheckInResult"
        )
        transaction?.addToBackStack("CheckInResult")
        transaction?.commit()
    }

    fun setBackButtonVisible(visible: Boolean) {
        if (visible) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            mToggle!!.isDrawerIndicatorEnabled = false
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            if (!mToolBarNavigationListenerIsRegistered) {
                mToggle!!.toolbarNavigationClickListener = View.OnClickListener {
                    onBackPressed()
                }
                mToolBarNavigationListenerIsRegistered = true
            }
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            mToggle!!.isDrawerIndicatorEnabled = true
            mToggle!!.toolbarNavigationClickListener = null
            mToolBarNavigationListenerIsRegistered = false
        }
    }

    fun setTitle(title: String? = null) {
        if (title == null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            imageViewLogo.visibility = View.VISIBLE
        } else {
            supportActionBar!!.title = title
            imageViewLogo.visibility = View.GONE
            supportActionBar!!.setDisplayShowTitleEnabled(true)

        }
    }
}
