package com.example.doodbluetask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bhuvanesh.appbase.getViewModel
import com.example.doodbluetask.foodmenu.FoodMenuViewModel

class MainActivity : AppCompatActivity() {

    // Shared view model
    private lateinit var mViewModel: FoodMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = getViewModel { FoodMenuViewModel(FoodDeliveryApplication.getAppContext()) }
    }
}
