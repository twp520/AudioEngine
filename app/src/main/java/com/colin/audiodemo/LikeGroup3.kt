package com.colin.audiodemo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * create by colin on 2019/1/31
 */
class LikeGroup3 : LinearLayout {

    private var mWidth = 0
    private var mHeight = 0
    private var mItems = hashMapOf<String, LikeItem>()
    private var mHandler: Handler


    constructor(context: Context) : super(context) {
        orientation = VERTICAL
        mHandler = Handler(Looper.getMainLooper())
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        orientation = VERTICAL
        mHandler = Handler(Looper.getMainLooper())
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }


    fun like(key: String) {
        if (mItems.contains(key)) { //包含这个key了，那么加数量
            mItems[key]?.addCount()
        } else { //如果未包含，则初始化
            val item = LikeItem(context)
            item.setText(key)
            item.setEndCall {
                //结束时移除
                removeView(it)
                mItems.remove(key)
            }
            item.addCount()
            addView(item)
            mItems[key] = item
        }

    }

}
