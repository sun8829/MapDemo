package com.ppdai.mapdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Created by sunhuahui on 2017/10/31.
 */

class DirectionFragment : BottomSheetDialogFragment() {
    private var mRoutes: ArrayList<Direction.Route>? = null
    private var mAdapter: DirectionAdapter? = null
    private var mPlanRv: RecyclerView? = null
    private var mCalorieTxt: TextView? = null
    private var mNavTxt: TextView? = null
    private var mStart: Direction.LatLn? = null
    private var mEnd: Direction.LatLn? = null
    private var mPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_direction, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mRoutes = arguments.getSerializable("routes") as ArrayList<Direction.Route>
        mPosition = arguments.getInt("position")
        mStart = arguments.getSerializable("start") as Direction.LatLn
        mEnd = arguments.getSerializable("end") as Direction.LatLn

        mNavTxt = view!!.findViewById<View>(R.id.nav) as TextView
        mCalorieTxt = view.findViewById<View>(R.id.calorie) as TextView
        mPlanRv = view.findViewById<View>(R.id.plan) as RecyclerView
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        val width = metric.widthPixels     // 屏幕宽度（像素）
        mAdapter = DirectionAdapter(mRoutes!!, width, mPosition)
        mPlanRv!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mPlanRv!!.adapter = mAdapter
        val kcal = 1.036f * 50f * Integer.parseInt(mRoutes!![mPosition].legs!![0].distance!!.value).toFloat() / 1000
        mCalorieTxt!!.text = kcal.toString() + " kcal"

        mNavTxt!!.setOnClickListener {
            var url = "http://maps.google.com/maps?saddr=${mStart!!.lat},${mStart!!.lng}&daddr=${mEnd!!.lat},${mEnd!!.lng}";
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + mStart!!.lat + "," + mStart!!.lng + "&daddr=" + mEnd!!.lat + "," + mEnd!!.lng))
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
            startActivity(intent)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        fun newInstance(routes: ArrayList<Direction.Route>, position: Int, start: Direction.LatLn, end: Direction.LatLn): DirectionFragment {
            val fragment = DirectionFragment()
            val bundle = Bundle()
            bundle.putSerializable("start", start)
            bundle.putSerializable("end", end)
            bundle.putSerializable("routes", routes)
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }
}
