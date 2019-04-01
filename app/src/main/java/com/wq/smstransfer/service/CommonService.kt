package com.wq.kotlin.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log

/**
 * @author wq
 * @date 2019/3/18 上午11:14
 * @desc ${TODO}
 */
class CommonService : Service() {


    override fun onCreate() {
        super.onCreate()
        Log.i("tag", "onCreate")
    }

    override fun onBind(p0: Intent?): IBinder? {

        Log.i("tag", "onBind")

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i("tag", "onStartCommand,startId:$startId")

        if (count > 0) {
            Log.i("tag", "current count is :$count")

            mHandler.removeMessages(0)

            stopSelf()

        } else {
            mHandler.sendEmptyMessage(0)

        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeMessages(0)

        Log.i("tag", "onDestroy")

    }

    var count = 0
    private var mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            Log.i("tag", "$count")
            count++
            if (count == 100) {

                this.removeMessages(0)
                stopSelf()
            } else {
                this.sendEmptyMessageDelayed(0, 1000)
            }

        }
    }


}