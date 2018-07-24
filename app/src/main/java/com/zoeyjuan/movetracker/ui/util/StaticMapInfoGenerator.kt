package com.zoeyjuan.movetracker.ui.util

import android.graphics.Color
import com.mypopsy.maps.StaticMap
import com.zoeyjuan.movetracker.database.UserLocation

class StaticMapInfoGenerator {
    fun createUrlStr(geoPointList: Array<StaticMap.GeoPoint>) = StaticMap()
            .size(600, 400)
            .path(StaticMap.Path.Style.builder().stroke(5).color(Color.BLUE).build(), *geoPointList)
            .marker(geoPointList.first(), geoPointList.last())
            .toString()

    // Sort GeoPoints by trackTime
    fun getGeoPointLitst(locations: MutableList<UserLocation>): Array<StaticMap.GeoPoint> {
        var geoPointList = mutableListOf<StaticMap.GeoPoint>()
        locations.sortedBy { it.trackTime }
                .forEach {
                    geoPointList.add(StaticMap.GeoPoint(it.latitude, it.longtitude))
                }
        return geoPointList.toTypedArray()
    }
}