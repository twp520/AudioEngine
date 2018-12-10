package com.colin.audiodemo

import android.media.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {

    private var mClient: WebSocketClient? = null
    private val TAG = "demo"
    private var mAudioRecord: AudioRecord? = null
    private lateinit var mAudioRecordBuffer: ByteArray
    private lateinit var mAudioPlayBuffer: ByteArray
    private var mAudioTrack: AudioTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_init.setOnClickListener {
            init()
        }

        main_start.setOnClickListener {
            startAudio()
        }

        main_stop.setOnClickListener {
            stopAudio()
        }

        main_destroy.setOnClickListener {
            closeIO()
        }
    }


    private fun init() {
        try {
            //连接到服务器
            Thread {
                mClient = object :
                    WebSocketClient(
                        URI.create("ws://192.168.31.8:8080/chatServer/test2"),
                        Draft_6455(),
                        mutableMapOf(),
                        20
                    ) {
                    override fun onOpen(handshakedata: ServerHandshake?) {
                        //链接打开
                        Log.e(TAG, "已经链接到服务器。${handshakedata?.httpStatusMessage}")

                    }

                    override fun onClose(code: Int, reason: String?, remote: Boolean) {
                        //链接关闭
                        Log.e(TAG, "连接关闭。code = $code  reason = $reason")
                    }

                    override fun onMessage(message: String?) {
                        //收到消息 字符串
                        Log.e(TAG, "收到消息 字符串 =  $message")
                    }

                    override fun onError(ex: Exception?) {
                        //发生错误
                        Log.e(TAG, "发生错误！")
                        ex?.printStackTrace()
                    }

                    override fun onMessage(bytes: ByteBuffer?) {
                        //收到消息字节流
                        Log.e(TAG, "收到消息 字节流 size = ${bytes?.limit()}")
                        playAudioStream(bytes)
                    }
                }
                mClient?.connect()
            }.start()
            //初始化录音器
            val frequence = 8000// 采样率 8000
            val channelInConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO// 定义采样通道
            val audioEncoding = AudioFormat.ENCODING_PCM_16BIT// 定义音频编码（16位）
            val bufferSize = AudioRecord.getMinBufferSize(frequence, channelInConfig, audioEncoding)
            //实例化AudioRecord
            mAudioRecord =
                    AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelInConfig, audioEncoding, bufferSize)
            //定义缓冲数组
            mAudioRecordBuffer = ByteArray(bufferSize)

            //初始化播放器
            val channelOutConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO
            val playBufferSize = AudioTrack.getMinBufferSize(frequence, channelOutConfig, audioEncoding)
            mAudioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC, frequence,
                channelOutConfig, audioEncoding, bufferSize,
                AudioTrack.MODE_STREAM
            )
            mAudioPlayBuffer = ByteArray(playBufferSize)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private var isRecording = false
    private fun startAudio() {
        Thread {
            mAudioRecord?.startRecording();// 开始录制

            isRecording = true// 设置录制标记为true
            //开始录制
            while (isRecording) {
                // 录制的内容放置到了buffer中，result代表存储长度
                val result = mAudioRecord?.read(mAudioRecordBuffer, 0, mAudioRecordBuffer.size)
                /*.....result为buffer中录制数据的长度(貌似基本上都是640)。剩下就是处理buffer了，是发送出去还是直接播放，这个随便你。*/
                sendAudioStream(result)
            }
            //录制循环结束后，记得关闭录制！！
            mAudioRecord?.stop()
        }.start()
    }

    private fun stopAudio() {
        isRecording = false
        mAudioRecord?.stop()
        mAudioRecord?.release()
    }

    private fun sendAudioStream(result: Int?) {
        mClient?.send(ByteBuffer.wrap(mAudioRecordBuffer, 0, result ?: 0))
//        PersonPB.Person.newBuilder().setType(1).setContent("test").build().toByteArray()

    }

    private fun playAudioStream(bytes: ByteBuffer?) {
        if (null != bytes) {
            mAudioTrack?.write(bytes, bytes.remaining(), AudioTrack.WRITE_NON_BLOCKING)
            mAudioTrack?.play()
        }
    }

    private fun sendText() {
        mClient?.send("你知道吗？我在发消息")
    }

    private fun closeIO() {
        mClient?.close()
    }
}
