package com.arbigaus.climahoje.Controller

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbigaus.climahoje.Interfaces.LoadImageReceiverDelegate
import com.arbigaus.climahoje.Interfaces.LoadReceiverDelegate
import com.arbigaus.climahoje.Model.City
import com.arbigaus.climahoje.Model.DataStore
import com.arbigaus.climahoje.Model.DayInfo
import com.arbigaus.climahoje.Model.WeatherInfo
import com.arbigaus.climahoje.R
import com.arbigaus.climahoje.View.DayInfoAdapter
import java.util.*

class MainActivity : AppCompatActivity(), LoadReceiverDelegate, LoadImageReceiverDelegate {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataStore.setContext(this)

        viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewAdapter = DayInfoAdapter(DataStore.infoDayList)

        recyclerView = findViewById<RecyclerView>(R.id.day_info_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        val favoriteCity = DataStore.getFavoriteCity()
        if (favoriteCity != null) {
            DataStore.getCurrentData(City(favoriteCity.name, true), this)
        }
        else {
            DataStore.getCurrentData(City("Curitiba", true), this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_search_city, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this@MainActivity, ManagerCityActivity::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    @ExperimentalStdlibApi
    override fun setUpdatedWeatherData(status: Boolean) {
        if (status) {
            val weatherInfo = DataStore.weatherInfo
            val degreeTextView = findViewById<TextView>(R.id.degreeTextView)
            val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
            val cityNameTextView = findViewById<TextView>(R.id.cityNameTextView)

            degreeTextView.text = "${weatherInfo?.degree}\u00B0"
            descriptionTextView.text = weatherInfo?.description?.capitalize(Locale.getDefault())
            cityNameTextView.text = weatherInfo?.cityName

            if (weatherInfo?.icon != null) {
                DataStore.getImageIcon(weatherInfo.icon + "@4x.png", this)
            }
            val params = arrayListOf<String>()
            val lonParam = "&lon=${weatherInfo?.lon}"
            val latParam = "&lat=${weatherInfo?.lat}"

            params.add(lonParam)
            params.add(latParam)

            DataStore.getForecastData(params, this)
        }
        else {
            Log.d("Error!", "Algo deu errado")
        }
    }

    override fun setUpdatedImage(status: Boolean, image: Bitmap?) {
        if (status) {
            val iconImageView = findViewById<ImageView>(R.id.iconImageView)
            iconImageView.setImageBitmap(image)
        }
        else {
            Log.d("Error!", "Algo deu errado")
        }
    }

    override fun setUpdatedForecastData(status: Boolean) {
        if (status) {
            viewAdapter.notifyDataSetChanged()
        }
        else {
            Log.d("Error!", "Algo deu errado")
        }
    }
}