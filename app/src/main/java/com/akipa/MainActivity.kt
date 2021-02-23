package com.akipa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.akipa.databinding.ActivityMainBinding
import com.akipa.utils.Certificado
import com.akipa.utils.Constantes

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EasyProm)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        Certificado.handleSSLHandshake()
        drawerLayout = binding.drawerLayout

        // Para manejar la navegación con el 'up button'
        val navController = findNavController(R.id.my_nav_host_fragment)

        // manipulando la maniobralidad del drawer
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _ ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                // cambio de menu dependiendo si se está logueado o no
                if (Constantes.personalAkipaLogueado != null) {
                    binding.navigationView.menu.clear()
                    binding.navigationView.inflateMenu(R.menu.cajero_drawer_menu)
                } else {
                    binding.navigationView.menu.clear()
                    binding.navigationView.inflateMenu(R.menu.cliente_drawer_menu)
                }
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navControler = findNavController(R.id.my_nav_host_fragment)
        return NavigationUI.navigateUp(navControler, drawerLayout)
    }
}