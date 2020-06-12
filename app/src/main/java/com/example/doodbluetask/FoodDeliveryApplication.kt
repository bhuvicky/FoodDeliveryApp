package com.example.doodbluetask

import android.app.Application

class FoodDeliveryApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        mInstance = this
    }

    companion object {
        private lateinit var mInstance: FoodDeliveryApplication

        fun getAppContext() = mInstance
    }
}