package com.colin.audiodemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    private var mAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val dir = CAudioEngine.getCacheDir()
        if (dir.isDirectory) {
            dir.list().forEach {
                Log.e("twp", it)
            }
        }
        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dir.list())
        test_list.adapter = mAdapter
        test_list.setOnItemClickListener { parent, view, position, id ->
            val fileName = test_list.adapter.getItem(position).toString()
            val filePath = dir.absolutePath + "/" + fileName
            if (CAudioEngine.isPlaying(filePath)) {
                CAudioEngine.stopPlay()
            } else {
                CAudioEngine.playFile(filePath)
            }
        }
//        test_wlv.setVolume(80)
//        test_wlv.startAnim()
    }


    fun startRecord(view: View) {
        CAudioEngine.startRecord()
    }

    fun stopRecord(view: View) {
        CAudioEngine.stopRecord()
        val dir = CAudioEngine.getCacheDir()
        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dir.list())
        test_list.adapter = mAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        CAudioEngine.onDestroy()
//        test_wlv.stopAnim()
//        test_wlv.release()
    }
}
