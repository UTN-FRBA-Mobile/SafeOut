package com.utn_frba_mobile_2020_c2.safeout.activities

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
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


class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, PlaceCommunicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
                setVisibleFragment(QrScannerFragment())
            }
            R.id.Checkin -> {
                val intent = Intent(this, NFCActivity::class.java)
                startActivity(intent)
            }
            R.id.drawerItemLogout -> {
                AuthController.logout()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
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

}
