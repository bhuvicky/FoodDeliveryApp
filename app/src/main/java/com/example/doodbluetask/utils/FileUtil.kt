package com.example.doodbluetask.utils

import android.content.Context
import com.example.doodbluetask.model.FoodMenu
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type

object FileUtil {

    fun <T> getFromAssetsFolder(context: Context, filename: String, type: Type?): T {
        return getFromAssetsFolder(context, filename, null, type)
    }

    fun <T> getFromAssetsFolder(context: Context, filename: String, clazz: Class<T>?): T {
        return getFromAssetsFolder(context, filename, clazz, null)
    }

    fun <T> getFromAssetsFolder(context: Context, jsonFileName: String,
                                       clazz: Class<T>? = null,
                                       type: Type? = null) : T {
        lateinit var json: String
        var result: T? = null

        try {
            val inputStream = context.getAssets().open(jsonFileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.use { it.read(buffer) }
            json = String(buffer)

            result = type?.let { Gson().fromJson<T>(json, type) } ?:
                    Gson().fromJson(json, clazz)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            json = ""
        }
        // ERROR: public inline func cannot access private fun "pareseJson"
        return result!!
    }

     fun parseJson(data: String): List<FoodMenu> {
        val gson = Gson()
        val jsonOutput = data
        val listType = object : TypeToken<List<FoodMenu?>?>() {}.type
        val noOfPage: List<FoodMenu> = gson.fromJson(jsonOutput, listType)
        println("log no of page = ${noOfPage.size}")
        return noOfPage
    }
}