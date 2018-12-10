package com.colin.audiodemo

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 播放核心
 * 提供播放音频功能
 */
class PlayCore {

    private lateinit var mPlayer: MediaPlayer
    private var mHandler = Handler(Looper.getMainLooper())
    private var mExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var mListener: OnPlayStateListener? = null

    fun reInit() {
        mPlayer = MediaPlayer()

        mPlayer.setOnErrorListener { mp, what, extra ->
            //播放错误
            Log.e("CAudioEngine", "播放器发生错误")
            return@setOnErrorListener mListener?.onError(what, extra) ?: false
        }

        mPlayer.setOnCompletionListener {
            //播放完成
            Log.e("CAudioEngine", "播放完成")
            mListener?.onPlayComplete()
        }

    }

    fun startPlayNetUrl(url: String) {
        stopPlay()
        mPlayer.reset()
        mPlayer.setDataSource(url)
        mPlayer.prepare()
        mPlayer.start()
        postProgress()
    }

    fun startPlayFile(path: String) {
        stopPlay()
        mPlayer.reset()
        mPlayer.setDataSource(path)
        mPlayer.prepare()
        mPlayer.start()
        postProgress()
    }

    fun startPlayAssets(fd: AssetFileDescriptor) {
        stopPlay()
        mPlayer.reset()
        mPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mPlayer.prepare()
        mPlayer.start()
        postProgress()
    }

    fun pausePlay() {
        if (mPlayer.isPlaying) {
            mPlayer.pause()
        }
    }

    fun stopPlay() {
        if (mPlayer.isPlaying) {
            mPlayer.stop()
            mListener?.onPlayStop()
        }
    }

    fun isPlaying(): Boolean {
        return mPlayer.isPlaying
    }

    fun setOnPlayStateListener(listener: OnPlayStateListener) {
        this.mListener = listener
    }

    private fun postProgress() {
        mListener?.onPlayStart()
        val duration = mPlayer.duration
        mExecutor.submit {
            while (mPlayer.isPlaying) {
                val progress = mPlayer.currentPosition.toFloat() / duration.toFloat() * 100f
                mHandler.post { mListener?.onProgress(progress.toInt()) }
                Thread.sleep(200)
            }
        }
    }

    fun onDestroy() {
        stopPlay()
        mPlayer.release()
    }

    interface OnPlayStateListener {

        fun onPlayStart()

        fun onProgress(progress: Int)

        fun onPlayComplete()

        fun onPlayStop()

        fun onError(what: Int, extra: Int): Boolean
    }

    open class SimplePlayStateListener : OnPlayStateListener {
        override fun onPlayStart() {

        }

        override fun onProgress(progress: Int) {

        }

        override fun onPlayComplete() {

        }

        override fun onPlayStop() {

        }

        override fun onError(what: Int, extra: Int): Boolean {
            return false
        }

    }
}