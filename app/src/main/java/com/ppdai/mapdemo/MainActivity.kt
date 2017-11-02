package com.ppdai.mapdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        google_path.setOnClickListener({
            startActivity(Intent(this@MainActivity, MapsActivity::class.java))
        })

        amap_path.setOnClickListener({
            startActivity(Intent(this@MainActivity, AMapPathActivity::class.java));
        })

        amapJs.setOnClickListener({
            var intent = Intent(this@MainActivity, AMapJsActivity::class.java)
            intent.putExtra("url", "file:///android_asset/route.html")
            startActivity(intent);
        })

        amapWeb.setOnClickListener({
            var intent = Intent(this@MainActivity, AMapJsActivity::class.java)
            intent.putExtra("url", "http://uri.amap.com/navigation?from=121.6255766963,31.2227267920,startpoint&to=121.5942460251,31.2131540761,endpoint&mode=walk&policy=1&src=mypage&coordinate=gaode&callnative=0")
            startActivity(intent);
        })

        amapMarker.setOnClickListener({
            var intent = Intent(this@MainActivity, AMapJsActivity::class.java)
            intent.putExtra("url", "file:///android_asset/marker.html")
            startActivity(intent);
        })
    }
}
