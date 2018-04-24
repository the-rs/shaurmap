package com.rightsoftware.shaurmap.utils.map

import android.content.res.Resources
import android.graphics.*
import android.support.v4.util.LruCache
import com.androidmapsextensions.ClusterOptions
import com.androidmapsextensions.ClusterOptionsProvider
import com.androidmapsextensions.Marker
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.rightsoftware.shaurmap.R

class ClusterOptionsProvider(resources: Resources) : ClusterOptionsProvider {

    companion object {
        private val res = intArrayOf(R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5)
        private val forCounts = intArrayOf(10, 100, 1000, 10000, Integer.MAX_VALUE)
    }

    private val baseBitmaps: List<Bitmap>
    private val cache = LruCache<Int, BitmapDescriptor>(128)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = Rect()

    private val clusterOptions = ClusterOptions().anchor(0.5f, 0.5f)

    init {
        baseBitmaps = res.map { BitmapFactory.decodeResource(resources, it) }

        with(paint){
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = resources.getDimension(R.dimen.text_size)
        }
    }

    override fun getClusterOptions(markers: List<Marker>): ClusterOptions {
        val markersCount = markers.size
        cache.get(markersCount)?.let { return clusterOptions.icon(it) }

        var base: Bitmap
        var i = 0
        do {
            base = baseBitmaps[i]
        } while (markersCount >= forCounts[i++])

        val bitmap = base.copy(Bitmap.Config.ARGB_8888, true)

        val text = markersCount.toString()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = bitmap.width / 2.0f
        val y = (bitmap.height - bounds.height()) / 2.0f - bounds.top

        Canvas(bitmap).apply { drawText(text, x, y, paint) }

        val icon = BitmapDescriptorFactory.fromBitmap(bitmap)
        cache.put(markersCount, icon)

        return clusterOptions.icon(icon)
    }
}