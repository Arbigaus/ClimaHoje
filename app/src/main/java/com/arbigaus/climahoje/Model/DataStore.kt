package com.arbigaus.climahoje.Model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.arbigaus.climahoje.Controller.ManagerCityActivity
import com.arbigaus.climahoje.Interfaces.LoadImageReceiverDelegate
import com.arbigaus.climahoje.Interfaces.LoadReceiverDelegate
import com.arbigaus.climahoje.R
import com.arbigaus.climahoje.Sevices.TaskLoadCurrentData
import com.arbigaus.climahoje.Sevices.TaskLoadForecastData
import com.arbigaus.climahoje.Sevices.TaskLoadImage
import java.io.File
import java.io.IOException

object DataStore {

    var  infoDayList: MutableList<DayInfo> = arrayListOf()
        private set

    var cities: MutableList<City> = arrayListOf()
        private set

    var weatherInfo: WeatherInfo? = null
    var iconImage: Bitmap? = null

    private var context: Context? = null

    fun addInfoItem(item: DayInfo) {
        infoDayList.add(item)
    }

    fun setContext(value: Context) {
        context = value
        loadCitiesFromFile()
    }

    fun addCity(city: City) {
        cities.add(city)
        updateCitiesFile()
    }

    fun getCity(position: Int): City {
        return cities[position]
    }

    fun updateCity(city: City, position: Int) {
        if (city.stared) {
            cities.forEach {
                it.stared = false
            }
        }
        cities[position] = city
        updateCitiesFile()
    }

    fun getFavoriteCity(): City? {
        return cities.find { it.stared }
    }

    fun removeCity(position: Int, managerCityActivity: ManagerCityActivity) {
        cities.removeAt(position)
        updateCitiesFile()
    }

    private fun loadCitiesFromFile() {
        val context = context ?: return

        try {
            val file = File(context.filesDir.absolutePath + "/${context.getString(R.string.filename_cities)}")

            if (file.exists()) {
                file.bufferedReader().use {
                    val iterator = it.lineSequence().iterator()
                    while (iterator.hasNext()) {
                        val name = iterator.next()
                        val stared = iterator.next().toBoolean()
                        cities.add(City(name, stared))
                    }
                }
            }
        } catch (e: IOException) {
            Log.w("Load File", "File not found: ${e.localizedMessage}")
        }
    }

    private fun updateCitiesFile() {
        val context = context ?: return
        val file = File(context.filesDir.absolutePath + "/${context.getString(R.string.filename_cities)}")

        if (!file.exists()) {
            file.createNewFile()
        }
        else {
            file.writeText("")
        }

        file.printWriter().use {
            for (city in cities) {
                it.println(city.name)
                it.println(city.stared.toString())
            }
        }
    }

    fun getCurrentData(city: City, delegate: LoadReceiverDelegate) {
        TaskLoadCurrentData(delegate, city.name).execute()
    }

    fun getImageIcon(image: String, delegate: LoadImageReceiverDelegate) {
        TaskLoadImage(delegate, image).execute()
    }

    fun getForecastData(params: List<String>, delegate: LoadReceiverDelegate) {
        TaskLoadForecastData(delegate, params).execute()
    }

}