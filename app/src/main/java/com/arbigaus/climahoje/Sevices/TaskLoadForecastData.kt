package com.arbigaus.climahoje.Sevices

import android.os.AsyncTask
import android.util.Log
import com.arbigaus.climahoje.Interfaces.LoadReceiverDelegate
import com.arbigaus.climahoje.Model.DataStore
import com.arbigaus.climahoje.Model.DayInfo
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TaskLoadForecastData(private var delegate: LoadReceiverDelegate, private val params: List<String>)
    : AsyncTask<String, Void, String>() {
    private val network = Network("", Request.forecast)

    override fun doInBackground(vararg p0: String?): String {
        network.setUrl(params)
        return network.getJsonObject()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        try {
            val json = JSONObject(result)
            val dailyList = json.getJSONArray("daily")

            for (i in 0 until dailyList.length()) {
                val daily = dailyList[i] as JSONObject

                val timestampDay = daily.getInt("dt")
                val temp = daily.getJSONObject("temp")
                val weather = daily.getJSONArray("weather")
                val weatherObject = weather[0] as JSONObject

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(timestampDay * 1000)

                val dayInfo = DayInfo(
                    temp.getDouble("min").toInt(),
                    temp.getDouble("max").toInt(),
                    date,
                    weatherObject.getString("description"),
                    weatherObject.getString("icon")
                )
                DataStore.addInfoItem(dayInfo)
            }
            delegate.setUpdatedForecastData(true)
        }
        catch (e: Exception) {
            Log.e("error!", "Json error: ${e.localizedMessage}")
            delegate.setUpdatedWeatherData(false)
        }
    }
}