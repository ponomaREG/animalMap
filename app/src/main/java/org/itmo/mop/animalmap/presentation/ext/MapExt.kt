package org.itmo.mop.animalmap.presentation.ext

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

fun GoogleMap.moveToStartLocation() {

    val saintPetersburg = LatLng(59.937500, 30.308611)
    moveCamera(CameraUpdateFactory.newLatLngZoom(saintPetersburg, 10f))
}