package com.wsy.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 *  author : wsy
 *  date   : 2023/4/6
 *  desc   : 水平进度条 view的高度至少是5倍的进度条宽度，否则文字无法显示
 */
class CustomViewHorProgress(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    var endPercent = 0F
    var progressBarHeight = 0F  //高度

    var paddingHor = 0F
    var text = ""
    var textSize = 1F
    var textColor = Color.BLACK

    private val paintRectF = Paint() //中间矩形画笔
    var paintColorRectF = Color.BLACK //中间矩形画笔颜色
    private var paintStyleRectF = Paint.Style.FILL  //中间矩形画笔样式（填充）
    var rectFColorArrayList = intArrayOf()    //矩形渐变颜色列表,需传参
    private val rectFColorStartPlaceList  //每个颜色值的起始位置,需传endPercent
        get() = floatArrayOf(
            0F, (endPercent * 0.01).toFloat(), (endPercent * 0.01).toFloat()
        )

//    private val startAndEndCircleR: Float
//        get() = progressBarHeight / 2  //首尾半圆半径，为矩形高度的一半，达到填充效果

    //跟随进度的同心圆的圆心
    private val concentricCircleCenterP: Pair<Float, Float>
        get() = Pair(
            ((width - 2 * paddingHor) * endPercent * 0.01).toFloat() + paddingHor,
            progressBarHeight
        )

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()//把当前的画布的状态进行保存，然后放入Canvas状态栈中
        //画中间矩形
        paintRectF.apply {
            color = paintColorRectF
            style = paintStyleRectF
            isAntiAlias = true//抗锯齿
        }
        //水平渐变色
        val linearGradient = LinearGradient(
            progressBarHeight / 2 + paddingHor,
            progressBarHeight / 2,
            width - progressBarHeight / 2 - paddingHor,
            progressBarHeight / 2 + progressBarHeight,
            rectFColorArrayList,
            rectFColorStartPlaceList,
            Shader.TileMode.CLAMP
        )
        paintRectF.shader = linearGradient
        val rectF = RectF(
            progressBarHeight / 2 + paddingHor,
            progressBarHeight / 2,
            width - progressBarHeight / 2 - paddingHor,
            progressBarHeight / 2 + progressBarHeight
        )
        canvas?.drawRect(rectF, paintRectF)

        //画0点扇形
        val rectFFanStart = RectF(
            0F + paddingHor,
            progressBarHeight / 2,
            progressBarHeight + paddingHor,
            progressBarHeight / 2 + progressBarHeight,
        )
        val fanPaintStart = Paint()
        fanPaintStart.color = rectFColorArrayList[0]
        fanPaintStart.isAntiAlias = true
        canvas?.drawArc(
            rectFFanStart,
            90F,
            180F,
            false,
            fanPaintStart
        )

        //画终点扇形
        val rectFFanEnd = RectF(
            width - progressBarHeight - paddingHor,
            progressBarHeight / 2,
            width.toFloat() - paddingHor,
            progressBarHeight / 2 + progressBarHeight,
        )
        val fanPaintEnd = Paint()
        fanPaintEnd.color = rectFColorArrayList.last()
        fanPaintEnd.isAntiAlias = true
        canvas?.drawArc(
            rectFFanEnd,
            270F,
            180F,
            false,
            fanPaintEnd
        )

        //跟随进度的外圆
        val progressCirclePaintOut = Paint()
        progressCirclePaintOut.color = rectFColorArrayList[1]
        progressCirclePaintOut.isAntiAlias = true
        canvas?.drawCircle(
            concentricCircleCenterP.first,
            concentricCircleCenterP.second,
            progressBarHeight,
            progressCirclePaintOut
        )
        //跟随进度的内圆
        val progressCirclePaintIn = Paint()
        progressCirclePaintIn.color = Color.WHITE
        progressCirclePaintIn.isAntiAlias = true
        canvas?.drawCircle(
            concentricCircleCenterP.first,
            concentricCircleCenterP.second,
            progressBarHeight / 3 * 2,
            progressCirclePaintIn
        )

        val paintText = Paint()
        paintText.apply {
            strokeWidth = 1F  //画笔宽度
            isAntiAlias = true
            style = Paint.Style.FILL    //样式
            textAlign = Paint.Align.CENTER  //居中对齐
            textSize = this@CustomViewHorProgress.textSize
            color = textColor
            isFakeBoldText = true   //粗体
        }

        //向上偏移2F否则文字底部会被覆盖部分
        canvas?.drawText(text, concentricCircleCenterP.first, height.toFloat() - 2F, paintText)

    }
}