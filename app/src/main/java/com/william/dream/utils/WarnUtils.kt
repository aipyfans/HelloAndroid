package com.william.dream.utils

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.widget.Toast

/**
 * Created by MrHou
 *
 * @on 2018/6/9  16:48
 * @email houzhongzhou@foxmail.com
 * @desc 震动和闪光灯闪烁
 */
@RequiresApi(Build.VERSION_CODES.M)
class WarnUtils private constructor(var context: Context) {

    private lateinit var mCameraManager: CameraManager
    private lateinit var mCameraID: String

    init {
        try {
            mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val list = mCameraManager.cameraIdList
            mCameraID = list[0]
        } catch (e: CameraAccessException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * 闪光灯闪烁
     */
    fun sos(time: Long, isVibrate: Boolean = true, isFlash: Boolean = true) {
        isVibrate(time, isVibrate)

        var isOn = true
        Thread(Runnable {
            val startTime = System.currentTimeMillis()
            while (isOn) {
                if (isFlash) toggleOnOffFlash(true)
                try {
                    Thread.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if (isFlash) toggleOnOffFlash(false)
                try {
                    Thread.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                val stopTime = System.currentTimeMillis()
                if (stopTime - startTime >= time) {
                    isOn = false
                    stop()
                }
            }
        }).start()
    }

    private fun toggleOnOffFlash(enable: Boolean) {
        try {
            mCameraManager.setTorchMode(mCameraID, enable)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isVibrate(time: Long, enable: Boolean) {
        if (enable) {
            play(time)
        } else {
            stop()
        }
    }

    companion object {

        private var instance: WarnUtils? = null
        private lateinit var vibrator:Vibrator

                @RequiresApi(api = Build.VERSION_CODES.M)
        fun getInstance(context: Context): WarnUtils {
            if (instance == null) {
                instance = WarnUtils(context)
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            return instance!!
        }

        /**
         * 开启震动
         */
        fun play(time: Long) {
            vibrator.vibrate(time)
        }

        /**
         * 停止震动
         */
        fun stop() {
            vibrator.cancel()
        }
    }


}
