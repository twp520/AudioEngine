package com.colin.audiodemo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout

/**
 *create by colin on 2018/12/11
 */
class LikeGroup2 : LinearLayout {

    private var mWidth = 0
    private var mHeight = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    private fun init() {
//        val father = ImageView(context)
//        father.setImageResource(R.drawable.icon_test_like)
//        val params = LayoutParams(
//            LayoutParams.WRAP_CONTENT,
//            LayoutParams.WRAP_CONTENT,
//            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
//        )
//        addView(father, params)

//        father.setOnClickListener {
//            startAnim(createHeart(context))
//        }
        orientation = VERTICAL
    }

    private fun createLikeView(context: Context, targetUrl: String): View {
//        val like = ImageView(context)
//        like.setImageResource(R.drawable.icon_test_like)
//        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
//        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
//        like.layoutParams = params
//        addView(like)

        val linear = LinearLayout(context)
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
//            Gravity.BOTTOM
        )
        linear.orientation = LinearLayout.HORIZONTAL
        linear.gravity = Gravity.CENTER_VERTICAL
        val targetIcon = ImageView(context)
/*        val paramsIcon = LayoutParams(QMUIDisplayHelper.dp2px(context, 40), QMUIDisplayHelper.dp2px(context, 40))
        targetIcon.layoutParams = paramsIcon
        GlideApp.with(context).load(targetUrl).optionalCircleCrop().into(targetIcon)
        linear.addView(targetIcon)

        val paramsText = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        paramsText.gravity = Gravity.CENTER
        paramsText.leftMargin = QMUIDisplayHelper.dp2px(context, 15)
        val nameTv = TextView(context)
        nameTv.layoutParams = paramsText
        nameTv.text = resources.getString(R.string.sognchu)
        nameTv.setTextColor(Color.WHITE)
        nameTv.textSize = 12f
//        nameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, , 0)
//        nameTv.compoundDrawablePadding = 10
        linear.addView(nameTv)

        val zanIcon = ImageView(context)
        val paramsZanIcon =
            LinearLayout.LayoutParams(QMUIDisplayHelper.dp2px(context, 25), QMUIDisplayHelper.dp2px(context, 25))
        zanIcon.setImageResource(R.mipmap.icon_room_like)
        paramsZanIcon.leftMargin = QMUIDisplayHelper.dp2px(context, 15)
        zanIcon.layoutParams = paramsZanIcon
        linear.addView(zanIcon)

//        var bot = 0
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
//            bot += child.measuredHeight
//        }
//        params.topMargin = bot
        linear.setBackgroundResource(R.drawable.room_like_bg)
        addView(linear, params)*/
        return linear
    }

    private fun startAnim(like: View) {
        //先用上升动画代替
        val tranY = ObjectAnimator.ofFloat(like, "translationY", 0f, -mHeight.toFloat())
        val alpha = ObjectAnimator.ofFloat(like, "alpha", 1f, 0f)
        val set = AnimatorSet()
        set.duration = 1000
        set.startDelay = 2000
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

    fun like(likeUser: String) {
        startAnim(createLikeView(context, likeUser))
    }
}