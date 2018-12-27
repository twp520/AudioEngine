package com.colin.audiodemo

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.View


/**
 *create by colin on 2018/12/27
 */
class MyVoiceLineView : View {

    private lateinit var mPaint: Paint
    private lateinit var mSecondPaint: Paint
    private var mPath = Path()
    private var defA = 2f //TODO 初始峰值与view的宽度相关 公式还未找到
    private var mA = defA
    private var mO = 0f
    private var mWidth = 0
    private var mVx = 0f
    private var mVxStep = 0f
    private var mHeight = 0
    private var mSeeped = 0.5f
    private var mAs = arrayOf(mA * 0.75f, mA * 0.55f, mA * 0.25f, mA * 0.2f)
    private val tag = "MyVoiceLineView"
    private lateinit var mHandler: Handler


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context, attributes)
    }

    private fun init(context: Context, attributes: AttributeSet? = null) {
        mPaint = Paint()
        mSecondPaint = Paint()
        initPaint(mPaint)
        initPaint(mSecondPaint, 1f)
        mHandler = Handler { msg ->
            mO -= mSeeped
            postInvalidate()
            mHandler.sendEmptyMessageDelayed(1, 50)
            return@Handler true
        }
    }

    private fun initPaint(paint: Paint, strokeWidth: Float = 5f) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        val pathEffect = CornerPathEffect(50f)
        paint.pathEffect = pathEffect
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mPath.reset()
        mVx = -mWidth / 2f
        mVxStep = mWidth / 12f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.save()
        canvas.translate(mWidth / 2f, mHeight / 2f)
        mA = defA
        drawLine(mPaint, mPath, canvas)
        for (i in 0 until mAs.size) {
            mA = mAs[i]
            drawLine(mSecondPaint, mPath, canvas)
        }
//        canvas.restore()
    }

    /**
     * 绘制线条
     * @param paint 画笔
     * @param path 路径
     * @param canvas 画布
     */
    private fun drawLine(paint: Paint, path: Path, canvas: Canvas) {
        path.reset()
        mVx = -mWidth / 2f
        var x = -3f
        path.moveTo(mVx, getY(mVx) * 30)
        while (x <= 3f) {
            //TODO 30 通过view的 高 来计算。公式还未找到
            path.lineTo(mVx, getY(x) * 30)
            x += 0.5f
            mVx += mVxStep
        }
        canvas.drawPath(path, paint)
    }

    /**
     *  y = A * sin(w*x+o)+p
     *
     *  A 振幅  通过音量来控制
     *  w 波宽  可以设置
     *  o 相位  通过他来控制线移动
     *  p 增量  控制 函数在 y 轴上的点
     *
     *  4 / 4 + x^4 衰减函数
     *
     *
     *  @param x x坐标
     *  @return y坐标
     */
    private fun getY(x: Float): Float {
        return mA * getAttenuation(x) * Math.sin(Math.PI * x + mO).toFloat()
    }

    /**
     * 获取衰减函数的值
     * @param x x坐标
     * @return 函数值
     */
    private fun getAttenuation(x: Float): Float {
        return (4 / (4 + Math.pow(x.toDouble(), 4.toDouble()))).toFloat()
    }

    fun startAnim() {
        //通过改变 相位来移动
        mHandler.sendEmptyMessage(1)

    }

    fun stopAnim() {
        mHandler.removeCallbacksAndMessages(null)
    }

    fun setVolume(volume: Int) {
        val v = if (volume > 100) 100 else volume
        //计算波峰
        defA = v / 50f
        mA = defA
        mAs[0] = mA * 0.75f
        mAs[1] = mA * 0.55f
        mAs[2] = mA * 0.25f
        mAs[3] = mA * 0.25f
        postInvalidate()
    }

}