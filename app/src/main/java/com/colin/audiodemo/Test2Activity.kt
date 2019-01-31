package com.colin.audiodemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_test2.*

class Test2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        test_btn1.setOnClickListener {
            test_like3.like("用户A")
        }

        test_btn2.setOnClickListener {
            test_like3.like("用户B")
        }

    }

//    fun jianshao(view: View) {
//        tcb.incrementProgressBy(-1)
//        tcb.startCountDown(30000)
//        Log.e("twp","count = ${test_lg.childCount}")
//        test_sbv.startAnim()
//    }
/*
    fun start(view: View) {
        test_mvlv.startAnim()
    }

    fun stop(view: View) {
        test_mvlv.stopAnim()
    }*/
}
