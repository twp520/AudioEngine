package com.colin.audiodemo

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.File


/**
 * 音频引擎
 * 支持录制音频
 * 播放音频
 * 对音频的控制
 */
object CAudioEngine {

    private var mRecordCore: RecordCore = RecordCore()
    private var mPlayCore: PlayCore = PlayCore()
    private var mFileEngine: AudioFileEngine = AudioFileEngine()
    private var curPlayKey = ""
    private val defFilePath = Environment.getExternalStorageDirectory().absolutePath + "/CAudioEngine"
    private var cacheFilePath = defFilePath

    init {
        //初始化各种资源
        mRecordCore.init()
        mPlayCore.reInit()
    }

    /**
     * 开始录音
     * @param path 自定义录音文件路径，如果为空，则默认生成地址
     * @return 录音文件地址
     */
    fun startRecord(path: String? = ""): String {
        val finalPath = if (null != path && !path.isBlank()) {
            path
        } else {
            val file = File(getCacheDirPath(), "cae_${System.currentTimeMillis()}.amr")
            if (!file.exists())
                file.createNewFile()
            file.absolutePath
        }
        mRecordCore.setRecordFilePath(finalPath)
        mRecordCore.startRecord()
        return finalPath
    }

    /**
     * 停止当前录音
     */
    fun stopRecord() {
        mRecordCore.stopRecord()
    }

    /**
     * 播放网络上的音频文件
     * @param netUrl 文件地址
     */
    fun playUrl(netUrl: String) {
        if (mFileEngine.has(netUrl)) {
            playFile(mFileEngine.getFilePath(netUrl))
            return
        }
        curPlayKey = netUrl
        mPlayCore.startPlayNetUrl(netUrl)
        //进行磁盘缓存
        mFileEngine.saveToSdcard(netUrl)
    }

    /**
     * 播放本地音频文件
     * @param filePath 本地文件路径
     */
    fun playFile(filePath: String) {
        curPlayKey = filePath
        mPlayCore.startPlayFile(filePath)
    }

    /**
     * 播放 Assets 文件夹中的文件
     * @param context 上下文对象
     * @param fileName 文件名
     */
    fun playAssets(context: Context, fileName: String) {
        val fd = context.assets.openFd(fileName)
        curPlayKey = fileName
        mPlayCore.startPlayAssets(fd)
    }

    /**
     * 是否正在播放
     * @return 是否播放
     */
    fun isPlaying(key: String): Boolean {
        return mPlayCore.isPlaying() && TextUtils.equals(curPlayKey, key)
    }

    /**
     * 暂定当前播放
     */
    fun pasuePlay() {
        mPlayCore.pausePlay()
    }

    /**
     * 停止当前播放
     */
    fun stopPlay() {
        mPlayCore.stopPlay()
        curPlayKey = ""
    }

    /**
     * 获取缓存文件夹地址
     * @return 路径
     */
    fun getCacheDirPath(): String {
        return if (cacheFilePath.isEmpty()) defFilePath else cacheFilePath
    }

    /**
     * 获取缓存文件夹
     * @return 文件
     */
    fun getCacheDir(): File {
        return File(getCacheDirPath())
    }

    /**
     * 设置缓存文件夹路径
     * @return 是否设置成功
     */
    fun setCacheDirPath(path: String): Boolean {
        val dir = File(path)
        if (dir.mkdirs() && dir.isDirectory) {
            cacheFilePath = path
            return true
        }
        return false
    }

    fun setAudioListener(listener: OnAudioListener) {
        mPlayCore.setOnPlayStateListener(listener)
        mRecordCore.setOnRecordStateListener(listener)
    }

    /**
     * 销毁
     */
    fun onDestroy() {
        mRecordCore.onDestroy()
        mPlayCore.onDestroy()
    }


    class OnAudioListener : PlayCore.SimplePlayStateListener(), RecordCore.OnRecordStateListener {

        override fun onStartRecord() {

        }

        override fun volumeChanged(volume: Int) {

        }

        override fun onError(e: Exception) {

        }

        override fun onPlayStart() {

        }

        override fun onProgress(progress: Int) {

        }

        override fun onPlayComplete() {

        }

        override fun onPlayStop() {

        }

        override fun onError(what: Int, extra: Int): Boolean {
            return super.onError(what, extra)
        }
    }
}