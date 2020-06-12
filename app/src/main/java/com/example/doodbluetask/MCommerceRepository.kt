package com.example.doodbluetask

import android.app.Application
import com.example.doodbluetask.model.FoodMenu
import com.example.doodbluetask.model.RestaurantAddress
import com.example.doodbluetask.utils.FileUtil
import com.google.gson.reflect.TypeToken

class MCommerceRepository(val application: Application) {

    fun getFoodMenu(): List<FoodMenu> {
        val listType = object : TypeToken<List<FoodMenu?>?>() {}.type
        return FileUtil.getFromAssetsFolder<List<FoodMenu>>(application, "menu_list.json", listType)
    }

    fun getRestaurantAddress() = FileUtil.getFromAssetsFolder(application, "address.json", RestaurantAddress::class.java)
}