package com.colin.audiodemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_test2.*

class Test2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)


    }

    fun jianshao(view: View) {
//        tcb.incrementProgressBy(-1)
        tcb.startCountDown(30000)
    }
}
