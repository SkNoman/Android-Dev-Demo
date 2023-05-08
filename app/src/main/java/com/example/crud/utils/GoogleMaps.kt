package com.example.crud.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object GoogleMaps {
    fun openGoogleMaps(context: Context, latitude: Double, longitude: Double) {
        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }
}