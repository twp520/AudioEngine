package com.colin.audiodemo

import okhttp3.OkHttpClient
import okhttp3.Request
import okio.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 音频文件磁盘缓存引擎
 * 提供将网络的音频文件下载到本地SD卡
 */
class AudioFileEngine {
    private var okHttpClient = OkHttpClient()
    private var mExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun saveToSdcard(url: String) {
        mExecutor.submit {
            val request = Request.Builder().url(url).get().build()
            val response = okHttpClient.newCall(request).execute()
            val body = response.body()
            if (response.isSuccessful && null != body) {
                val inputStream = body.byteStream()
                //通过MD5生成文件名
                val file = File(CAudioEngine.getCacheDirPath(), "${MD5.encrypt(url)}.amr")
                if (!file.exists())
                    file.createNewFile()
                while (inputStream.read() != -1) {
                    file.sink().buffer().writeByte(inputStream.read())
                }
            }
        }
    }

    fun has(url: String): Boolean {
        return File(CAudioEngine.getCacheDirPath(), "${MD5.encrypt(url)}.amr").exists()
    }

    fun getFilePath(url: String): String {
        return File(CAudioEngine.getCacheDirPath(), "${MD5.encrypt(url)}.amr").absolutePath
    }
}