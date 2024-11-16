package com.example.myapplication.utils

import android.app.AlertDialog
import android.content.Context
import com.example.myapplication.R
import java.time.format.DateTimeFormatter

object Utils {

    val citiesList = mutableListOf<String>()
    val cityCountryMap = mutableMapOf<String, String>()

    fun addCity(city: String, country: String) {
        val checkCitiesList = citiesList.filter { it == city }
        if (checkCitiesList.isNotEmpty()) {
            citiesList.remove(city)
            citiesList.add(city)
        } else
            citiesList.add(city)
        if (cityCountryMap[city] != country)
            cityCountryMap[city] = country
    }

    fun formatDateView(currentDate: String): String {
        val listValues = currentDate.split("-")
        return ("${listValues[2]}.${listValues[1]}.${listValues[0]}")
    }

    fun getCurrentDateOfSpecificTimezone(timeZone: String): String? {
        val zoneId: java.time.ZoneId = java.time.ZoneId.of(timeZone)
        return java.time.ZonedDateTime.now(zoneId).format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy")
        )
    }

    fun showAlert(message: String, context: Context) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(context)
        builder
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}