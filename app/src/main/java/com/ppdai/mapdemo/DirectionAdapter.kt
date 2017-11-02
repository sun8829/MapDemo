package com.ppdai.mapdemo

import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.util.*

/**
 * Created by sunhuahui on 2017/10/31.
 */

class DirectionAdapter(data: ArrayList<Direction.Route>, private val mWidth: Int, private var mPosition: Int) : BaseQuickAdapter<Direction.Route, BaseViewHolder>(R.layout.item_detail, data) {

    override fun convert(helper: BaseViewHolder, item: Direction.Route) {
        helper.setText(R.id.time, item.legs!![0].duration!!.text)
                .setText(R.id.dis, item.legs!![0].distance!!.text!!)

        if (helper.adapterPosition == mPosition) {
            helper.setText(R.id.program, "推荐方案")
            helper.setBackgroundRes(R.id.bottom_detail, R.drawable.item_bg)
            helper.setBackgroundColor(R.id.program, Color.parseColor("#4182FF"))
            helper.setTextColor(R.id.program, Color.parseColor("#FFFFFF"))
        } else {
            helper.setText(R.id.program, "方案" + (helper.adapterPosition + 1))
            helper.setBackgroundRes(R.id.bottom_detail, R.drawable.item_bg_p)
            helper.setBackgroundColor(R.id.program, Color.parseColor("#E8E8E8"))
            helper.setTextColor(R.id.program, Color.parseColor("#838383"))
        }
        val root = helper.getView<FrameLayout>(R.id.root)
        val params = FrameLayout.LayoutParams(mWidth / mData.size, ViewGroup.LayoutParams.MATCH_PARENT)
        root.layoutParams = params
    }
}
