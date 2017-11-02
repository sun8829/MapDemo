package com.ppdai.mapdemo

import android.app.Application
import com.yanzhenjie.nohttp.NoHttp

/**
 * Created by sunhuahui on 2017/10/31.
 */
class App : Application() {

    override fun onCreate() {
        NoHttp.initialize(this)
        super.onCreate()
    }
}