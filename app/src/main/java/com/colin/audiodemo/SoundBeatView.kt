package com.colin.audiodemo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*
import kotlin.math.roundToInt

/**
 *create by colin on 2018/12/19
 *
 * //根据宽度，添加合适的view进去
 */
class SoundBeatView : LinearLayout {

    private val tag = "SoundBeatView"
    private var mWidth = 0
    private var mHeight = 0
    private var mLineWidth = 0
    private var mLineHeight = 0
    private var mLeftMargin = 0
    private var delays = arrayOf(200L, 100L, 300L, 400L)
    private var mRandom = Random()
    private var mAnimSet = AnimatorSet()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init()
    }


    private fun init() {
        //默认两2dp宽，间隔1dp
        orientation = HORIZONTAL
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt()
        mLeftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics).toInt()

        mAnimSet.duration = 1000
        mAnimSet.interpolator = LinearInterpolator()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.e(tag, "onFinishInflate")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.e(tag, "onSizeChanged")
        mWidth = w
        mHeight = h
        mLineHeight = h
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.e(tag, "onLayout")
        Log.e(tag, "child size = $childCount")
    }


    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        Log.e(tag, "onWindowFocusChanged  hasWindowFocus = $hasWindowFocus")
        if (hasWindowFocus) {
            val count = (mWidth / (mLineWidth.toFloat() + mLeftMargin)).roundToInt()
            for (i in 0 until count - 1) {
                addView(createLine())
            }
        }
    }


    private fun createLine(): View {
        val view = TextView(context)
        val params = LayoutParams(mLineWidth, LayoutParams.MATCH_PARENT)
        view.setBackgroundResource(R.drawable.shape_bg_round_top)
        params.leftMargin = mLeftMargin
        view.layoutParams = params
        view.pivotY = mLineHeight.toFloat()
        val scaleY = ObjectAnimator.ofFloat(
            view, "scaleY", 0f,
            mRandom.nextFloat(),
            mRandom.nextFloat(),
            mRandom.nextFloat(),
            mRandom.nextFloat(), 0f
        )
        scaleY.startDelay = delays[mRandom.nextInt(4)]
        scaleY.repeatCount = -1
        mAnimSet.playTogether(scaleY)
        return view
    }

    fun startAnim() {
        mAnimSet.start()
    }

    fun stopAnim() {
        mAnimSet.cancel()
    }
}