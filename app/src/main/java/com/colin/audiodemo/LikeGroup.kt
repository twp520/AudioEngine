package com.colin.audiodemo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView

/**
 *create by colin on 2018/12/11
 */
class LikeGroup : FrameLayout {

    private var mWidth = 0
    private var mHeight = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    private fun init(context: Context) {
        val father = ImageView(context)
        father.setImageResource(R.drawable.icon_test_like)
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        )
        addView(father, params)

        father.setOnClickListener {
            startAnim(createHeart(context))
        }
    }

    private fun createHeart(context: Context): ImageView {
        val like = ImageView(context)
        like.setImageResource(R.drawable.icon_test_like)
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        )
        like.layoutParams = params
        addView(like)
        return like
    }

    private fun startAnim(like: ImageView) {
        //先用上升动画代替
        val tranY = ObjectAnimator.ofFloat(like, "translationY", 0f, -mHeight.toFloat())
        val alpha = ObjectAnimator.ofFloat(like, "alpha", 1f, 0f)
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
                removeView(like)
            }

        })
        set.start()
    }
}