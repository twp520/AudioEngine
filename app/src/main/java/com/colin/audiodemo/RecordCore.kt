package com.colin.audiodemo

import android.media.MediaRecorder
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 录制核心
 * 提供录音功能
 */
class RecordCore {

    private var mRecord: MediaRecorder = MediaRecorder()
    private val defFormat = MediaRecorder.OutputFormat.AMR_NB
    private val defEncoder = MediaRecorder.AudioEncoder.AMR_NB
    private var isRecording = false
    private var mHandler = Handler(Looper.getMainLooper())
    private var mExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var mListener: OnRecordStateListener? = null
    private val baseDB = 600

    fun init() {
        mRecord.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecord.setOutputFormat(defFormat)
        mRecord.setAudioEncoder(defEncoder)
    }

    fun setRecordFilePath(path: String) {
        mRecord.reset()
        init()
        mRecord.setOutputFile(path)
    }

    fun startRecord() {
        try {
            if (isRecording)
                return
            isRecording = true
            mRecord.prepare()
            mRecord.start()
            mListener?.onStartRecord()
            mExecutor.submit {
                while (isRecording) {
                    //计算分贝
                    // int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;
                    val ratio = mRecord.maxAmplitude / baseDB
                    var db = 0// 分贝
                    if (ratio > 1) db = (20 * Math.log10(ratio.toDouble())).toInt()
                    mHandler.post { mListener?.volumeChanged(db) }
                    Thread.sleep(200)
                }
            }
        } catch (e: Exception) {
            Log.e("CAudioEngine", "录音发生错误")
            e.printStackTrace()
            mListener?.onError(e)
        }

    }


    fun stopRecord() {
        if (!isRecording)
            return
        isRecording = false
        mRecord.stop()
    }

    fun onDestroy() {
        if (isRecording)
            stopRecord()
        mRecord.release()
        mExecutor.shutdown()
    }

    fun setOnRecordStateListener(listener: OnRecordStateListener) {
        mListener = listener
    }

    interface OnRecordStateListener {
        fun onStartRecord()
        fun volumeChanged(volume: Int)
        fun onError(e: Exception)
    }
}