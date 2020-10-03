package com.arbigaus.climahoje.Sevices

import android.os.AsyncTask
import android.util.Log
import com.arbigaus.climahoje.Interfaces.LoadReceiverDelegate
import com.arbigaus.climahoje.Model.DataStore
import com.arbigaus.climahoje.Model.WeatherInfo
import org.json.JSONObject
import java.lang.Exception

class TaskLoadCurrentData(private var delegate: LoadReceiverDelegate, city: String)
    : AsyncTask<String, Void, String>() {
    private val network = Network(city, Request.current)

    override fun doInBackground(vararg p0: String?): String {
        return network.getJsonObject()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        try {
            val json = JSONObject(result)
            val weather = json.getJSONArray("weather")
            val data = weather[0] as JSONObject
            val citName = json.getString("name")
            val main = json.getJSONObject("main")
            var coord = json.getJSONObject("coord")

            DataStore.weatherInfo = WeatherInfo(
                main.getDouble("temp").toInt(),
                data.getString("description"),
                data.getString("icon"),
                citName,
                coord.getDouble("lon"),
                coord.getDouble("lat")
            )
            delegate.setUpdatedWeatherData(true)
        }
        catch (e: Exception) {
            Log.e("error!", "Json error: ${e.localizedMessage}")
            delegate.setUpdatedWeatherData(false)
        }
    }

}