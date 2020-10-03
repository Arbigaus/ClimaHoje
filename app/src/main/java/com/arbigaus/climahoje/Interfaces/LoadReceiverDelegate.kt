package com.arbigaus.climahoje.Interfaces

interface LoadReceiverDelegate {
    fun setUpdatedWeatherData(status: Boolean)
    fun setUpdatedForecastData(status: Boolean)
}