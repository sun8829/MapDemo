package com.ppdai.mapdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.overlay.WalkRouteOverlay
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.amap.api.services.route.RouteSearch.WalkRouteQuery
import kotlinx.android.synthetic.main.activity_amap_path.*


class AMapPathActivity : AppCompatActivity(), RouteSearch.OnRouteSearchListener {
    private var routeSearch: RouteSearch? = null
    private var aMap: AMap? = null
    private val mStartPoint = LatLonPoint(31.2227267920, 121.6255766963)
    private val mEndPoint = LatLonPoint(31.2131540761, 121.5942460251)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amap_path)

        routeSearch = RouteSearch(this)
        amapView.onCreate(savedInstanceState)

        if (aMap == null) {
            aMap = amapView.map
        }

        routeSearch?.setRouteSearchListener(this)
        setFromAndToMarker()
        var fromAndTo: RouteSearch.FromAndTo = RouteSearch.FromAndTo(mStartPoint, mEndPoint)
        val query = WalkRouteQuery(fromAndTo)
        routeSearch?.calculateWalkRouteAsyn(query)
    }

    override fun onResume() {
        amapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        amapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        amapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        amapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    private fun setFromAndToMarker() {
        aMap?.addMarker(MarkerOptions()
                .position(convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_blue)))
        aMap?.addMarker(MarkerOptions()
                .position(convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end_green)))
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    fun convertToLatLng(latLonPoint: LatLonPoint): LatLng {
        return LatLng(latLonPoint.latitude, latLonPoint.longitude)
    }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size > 0) {
                    var mWalkRouteResult = result
                    val walkPath = mWalkRouteResult.getPaths().get(0)
                    val walkRouteOverlay = WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos())
                    walkRouteOverlay.removeFromMap()
                    walkRouteOverlay.addToMap()
                    walkRouteOverlay.zoomToSpan()
                }

            }
        }
    }
}
