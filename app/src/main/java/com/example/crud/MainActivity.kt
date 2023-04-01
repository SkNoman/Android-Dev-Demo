package com.example.crud

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.crud.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        drawerLayout = binding.drawerLayout

        //OPEN NAV DRAWER FROM DEFAULT TOOLBAR
         toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //OPEN NAV DRAWER FROM CUSTOM TOOLBAR BUTTON
       /* binding.btnMenu.setOnClickListener {
           // val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
            val drawerLayout = binding.drawerLayout
            drawerLayout.openDrawer(GravityCompat.START)
        }*/

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navView: NavigationView = binding.navigationView

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.users -> navController.navigate(R.id.fragmentUserDashboard)
                R.id.item2 -> Toast.makeText(this,"Clicked Menu 2",Toast.LENGTH_SHORT).show()
                R.id.item3 -> Toast.makeText(this,"Clicked Menu 3",Toast.LENGTH_SHORT).show()
            }
            true
        }


        //CHANGE THE VIEW OR ACTIVITIES IN DIFFERENT FRAGMENTS
        navController.addOnDestinationChangedListener{controller,destination,arguments ->
            when(destination.id){
                R.id.fragmentSplash ->{
                    //supportActionBar?.hide()
                    binding.toolbar.visibility = View.GONE
                    // Set the window flags to hide the status bar and enable full screen mode
                    //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    // Disable the navigation drawer
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                }else -> {
                //supportActionBar?.show()
               // binding.toolbar.visibility = View.VISIBLE
                // Set the window flags to hide the status bar and enable full screen mode
                //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                // Disable the navigation drawer
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        try {
            if (navController.currentDestination?.id == R.id.fragmentLogin){
                finish()
            }else{
                super.onBackPressed()
            }
        }catch (e:Exception){
            Log.e("nlogE",e.toString())
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}