package com.arbigaus.climahoje.Sevices

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

val apiKey = "cd4061389aef116896459c7f7b4c78e1"

enum class Request(val endpoint: String) {
    current("weather?appid=${apiKey}&lang=pt_br&units=metric&q="),
    forecast("onecall?appid=${apiKey}&lang=pt_br&units=metric&&exclude=hourly,minutely,current&"),
    icon("")
}

class Network(params: String, request: Request, isIcon: Boolean = false) {
    private var baseUrl = "https://api.openweathermap.org/data/2.5/"
    private var urlStr = baseUrl + request.endpoint + params
    private var url: URL = URL(urlStr)
    private var connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection

    init {
        if (isIcon) {
            baseUrl = "https://openweathermap.org/img/wn/"
            url = URL(baseUrl + params)
            connection = url.openConnection() as HttpsURLConnection
        }
    }

    fun setUrl(params: List<String>) {
        for (param in params) {
            urlStr += param
        }
        url = URL(urlStr)
        connection = url.openConnection()  as HttpsURLConnection
    }

    fun getJsonObject() : String {
        var res = ""

        try {
            connection.connect()
            val inputStream = connection.inputStream
            res = inputStream.use { it.reader().use { reader -> reader.readText() } }
            inputStream.close()
        }
        catch (e: Exception) {
            Log.e("error!", e.localizedMessage)
        } finally {
            connection.disconnect()
        }
        return res
    }

    fun getImageBitmap() : Bitmap? {
        var bitmap: Bitmap? = null

        try {
            connection.connect()
            val inputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
        catch (e: Exception) {
            Log.e("error!", e.localizedMessage)
        }
        finally {
            connection.disconnect()
        }
        return bitmap
    }

}