package com.example.doodbluetask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.bhuvanesh.appbase.getViewModel
import com.bhuvanesh.appbase.ui.BaseFragment
import com.example.doodbluetask.foodmenu.FoodMenuViewModel
import com.example.doodbluetask.mycart.MyCartFragment

class MainActivity : AppCompatActivity() {

    // Shared view model
    private lateinit var mViewModel: FoodMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = getViewModel { FoodMenuViewModel(FoodDeliveryApplication.getAppContext()) }

        val navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onBackPressed() {
        /*if (sendBackPressToDrawer()) {
            return;
        }*/

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount ?: 0
        println("log count = $backStackEntryCount")

        // get Current Visible fragment
        val fragment = navHostFragment?.childFragmentManager?.fragments?.let { it[0] } as BaseFragment
        if (fragment is MyCartFragment)
            fragment.onBackPressed()

        // below method used to pop the current fragment
        findNavController(R.id.nav_host_fragment).navigateUp()

        if (backStackEntryCount > 0) {
            return;
        }
        finish()
    }
}
