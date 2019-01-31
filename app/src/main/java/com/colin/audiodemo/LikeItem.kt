package com.colin.audiodemo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView

/**
 * create by colin on 2019/1/31
 */
class LikeItem : LinearLayout {

    private lateinit var mNameTv: TextView
    private lateinit var mCountTv: TextView
    private var mCount = 0
    private var mHandler: Handler
    private var endCall: ((v: View) -> Unit)? = null
    private var handlerCallback = Handler.Callback {
        //message
        if (it.what == 0x31) {
            //开始动画
            startAnim()
        }
        return@Callback true
    }

    constructor(context: Context) : super(context) {
        init(context)
        mHandler = Handler(Looper.getMainLooper(), handlerCallback)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        mHandler = Handler(Looper.getMainLooper(), handlerCallback)
    }

    private fun init(context: Context) {
        orientation = LinearLayout.HORIZONTAL
        setBackgroundColor(Color.BLUE)
        //添加名字
        mNameTv = TextView(context)
        val paramsText = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        paramsText.gravity = Gravity.CENTER
        paramsText.leftMargin = 45
        mNameTv.layoutParams = paramsText
        mNameTv.setTextColor(Color.WHITE)
        mNameTv.textSize = 12f
        addView(mNameTv)
        //添加数量

        mCountTv = TextView(context)
        val paramsCount = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        paramsCount.gravity = Gravity.CENTER
        paramsCount.leftMargin = 45
        mCountTv.layoutParams = paramsCount
        mCountTv.setTextColor(Color.WHITE)
        mCountTv.textSize = 12f
        addView(mCountTv)
    }

    fun setText(text: String) {
        mNameTv.text = text
    }

    fun setEndCall(call: (v: View) -> Unit) {
        this.endCall = call
    }

    fun addCount() {
        mHandler.removeCallbacksAndMessages(null)
        mCountTv.text = mCount++.toString()
        mHandler.sendEmptyMessageDelayed(0x31, 3000)
    }

    private fun startAnim() {
        //先用上升动画代替
        val tranY = ObjectAnimator.ofFloat(this, "translationY", 0f, -100f)
        val alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        val set = AnimatorSet()
        set.duration = 1000
        set.interpolator = DecelerateInterpolator()
        set.playTogether(tranY, alpha)
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                endCall?.invoke(this@LikeItem)
            }

        })
        set.start()
    }

}
