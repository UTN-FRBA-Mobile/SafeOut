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
import com.utn_frba_mobile_2020_c2.safeout.services.CheckinService
import com.utn_frba_mobile_2020_c2.safeout.utils.GlobalUtils
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar.*

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, PlaceCommunicator {
    private var mToggle: ActionBarDrawerToggle? = null
    private var mToolBarNavigationListenerIsRegistered = false
    private var nfcPendingIntent: PendingIntent? = null
    private var nfcAdapter : NfcAdapter? = null

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

        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            GlobalUtils.backStackSize -= 1
            if (GlobalUtils.backStackSize >= 0) {
                super.onBackPressed()
                if (GlobalUtils.backStackSize == 0) {
                    setBackButtonVisible(false)
                }
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
                val mode = if(GlobalUtils.checkedInSection !== null) "CHECKOUT" else "CHECKIN"
                setVisibleFragment(QrScannerFragment.newInstance(mode))
            }
            R.id.CheckinNFC -> {
                if (nfcAdapter == null) {
                    // Esto es momentaneo hasta que se unifique el boton del checkin
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("NFC incompatible")
                    builder.setCancelable(true)
                    builder.setMessage("Tu dispositivo es incompatible para el uso de NFC. Pruebe con otra forma.")
                    builder.setPositiveButton("OK") { _, _ ->
                    }
                    builder.show()
                } else {
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
            // aca va  a logica para registrar la entrada

            val tag = checkIntent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            var idLugar: Long = deHexadecimalAEntero(byteArrayToHexString(tag?.id))
/*          INFO EXTRA
            var techList = tag?.techList
            println("Tecnologías usadas por la tarjeta :" + tag?.techList?.component1().toString())
            tag?.id --> es un ByteArray
 */
            println("Tecnologías usadas por la tarjeta : " + tag.toString())
            println("ID Tag leido del lugar en Decimal : " + idLugar)
            println("ID Tag leido del lugar en HEXA    : " + byteArrayToHexString(tag?.id))

            //TODO: Map to ID NFC
            val placeId = "5f600c75db23bc5159a7ed44";
            val sectionId = "5fa2fb64f434715c664c5d15";
            val mode = if(GlobalUtils.checkedInSection !== null) "CHECKOUT" else "CHECKIN"

            //Toast.makeText(this, "Check in exitoso, Bienvenido!", Toast.LENGTH_LONG).show()

            if(mode == "CHECKOUT"){
                CheckinService.checkOutOfSection(sectionId) { _, error ->
                    if (error != null) {
                        //ViewUtils.showSnackbar(, error)
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                        goToCheckinResultError(mode, error)
                    } else {
                        goToCheckinResultSuccess(mode, placeId, sectionId)
                    }
                }
            }else{
                CheckinService.checkInToSection(sectionId) { _, error ->
                    if (error != null) {
                        //ViewUtils.showSnackbar(, error)
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                        goToCheckinResultError(mode, error)
                    } else {
                        goToCheckinResultSuccess(mode, placeId, sectionId)
                    }
                }
            }

        } else {
            Toast.makeText(this, "Error, vuelva a intentar", Toast.LENGTH_LONG).show()
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
