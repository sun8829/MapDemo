package com.ppdai.mapdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.amap.api.maps2d.CoordinateConverter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.yanzhenjie.nohttp.rest.AsyncRequestExecutor
import com.yanzhenjie.nohttp.rest.Response
import com.yanzhenjie.nohttp.rest.SimpleResponseListener
import com.yanzhenjie.nohttp.rest.StringRequest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val COLORS = intArrayOf(R.color.primary_dark, R.color.primary, R.color.accent, R.color.primary_light, R.color.primary_dark_material_light)
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        getRoutes();
    }


    private fun getRoutes() {
        var converter = CoordinateConverter()
        converter.from(CoordinateConverter.CoordType.MAPABC)
        val request = StringRequest("https://maps.googleapis.com/maps/api/directions/json?origin=31.2227267920,121.6255766963&destination=31.2131540761,121.5942460251&mode=walking&alternatives=true&key=AIzaSyBTUbdcPKElh4aKq4Tyj_6c-8IBrbXKIBQ")
        AsyncRequestExecutor.INSTANCE.execute(0, request, object : SimpleResponseListener<String>() {
            override fun onSucceed(what: Int, response: Response<String>?) {
                val direction = Gson().fromJson(response!!.get(), Direction::class.java)
                if ("OK" != direction.status) {
                    Toast.makeText(this@MapsActivity, direction.status, Toast.LENGTH_LONG).show()
                    return
                }
                direction.parse()
                var route: Direction.Route? = null
                var mRoutes = direction.routes
                for (i in mRoutes.indices) {
                    val colorIndex = i % COLORS.size
                    if (route == null) {
                        route = mRoutes?.get(i)
                    }
                    val polyOptions = PolylineOptions()
                    polyOptions.color(resources.getColor(COLORS[colorIndex]))
                    polyOptions.width(20f)
                    polyOptions.addAll(mRoutes?.get(i)?.points)
                    val polyline = mMap.addPolyline(polyOptions)
                    polyline.isClickable = true
                }
                if (route == null || route!!.latLgnBounds == null) return
                var options = MarkerOptions()
                options.position(route!!.latLgnBounds.northeast)
                options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_blue))
                mMap.addMarker(options)

                options = MarkerOptions()
                options.position(route!!.latLgnBounds.southwest)
                options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.end_green))
                mMap.addMarker(options)
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(route!!.latLgnBounds, 150))

                val fragment = DirectionFragment.newInstance(mRoutes, 0, mRoutes.get(0).legs.get(0).start_location, mRoutes.get(0).legs.get(0).end_location)
                fragment.show(supportFragmentManager, DirectionFragment::class.java!!.getSimpleName())
            }

            override fun onFailed(what: Int, response: Response<String>?) {
                // 请求失败。
            }
        })
    }
}
