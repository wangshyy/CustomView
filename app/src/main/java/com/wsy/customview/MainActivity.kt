package com.wsy.customview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<CustomViewHorProgress>(R.id.custom_hp)
        view.apply {
            progressBarHeight = 20F   //进度条宽度
            endPercent = 50 //设置进度
            text = "123"
            textSize = 12F
            //设置渐变颜色
            rectFColorArrayList = intArrayOf(
                Color.parseColor("#13CF8D"),
                Color.parseColor("#D6EE02"),
                Color.parseColor("#F3F5F8"),
            )
        }
    }
}