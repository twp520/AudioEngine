package com.colin.audiodemo

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * create by colin on 2019/1/31
 */
class LikeGroup3 : LinearLayout {

    private var mWidth = 0
    private var mHeight = 0
    private var mItems = hashMapOf<String, LikeItem>()


    constructor(context: Context) : super(context) {
        orientation = VERTICAL
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        orientation = VERTICAL
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }


    /**
     * 当收到点赞消息时候，调用这个方法
     * @param key 标志唯一性的key，可以用userID
     *
     * 也可以加其他UI需要的参数
     */
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
