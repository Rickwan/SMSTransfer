package com.wq.kotlin.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import com.wq.smstransfer.net.NetworkRequest
import com.wq.smstransfer.utils.PhoneUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author wq
 * @date 2019/3/18 上午11:14
 * @desc ${TODO}
 */
class CommonService : Service() {

    private var lastCallState = TelephonyManager.CALL_STATE_IDLE

    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate() {
        super.onCreate()
        Log.i("tag", "onCreate")

    }

    override fun onBind(p0: Intent?): IBinder? {

        Log.i("tag", "onBind")

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        telephony()
        return super.onStartCommand(intent, flags, startId)
    }


    private fun telephony() {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            tm.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {

                    if (lastCallState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE) {

                        queryPhoneNumber(phoneNumber!!)
                        lastCallState = TelephonyManager.CALL_STATE_IDLE
                    }else{
                        lastCallState = state
                    }

                    Log.i("tag","未接来电：$state")
                    super.onCallStateChanged(state, phoneNumber)
                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        } catch (e: Exception) {
            lastCallState = TelephonyManager.CALL_STATE_IDLE
            Log.i("tag","异常")
        }

    }

    private fun queryPhoneNumber(phoneNumber: String) {

        var contactName = PhoneUtils.queryContact(this, phoneNumber)

        if (TextUtils.isEmpty(contactName)) {
            return
        }
        var date = format.format(Date(System.currentTimeMillis()))
        var text = "您有未接来电！"
        var des = "您有未接来电！，{$contactName}{$phoneNumber}于{$date}给您来电，请注意查看！"

        Log.i("tag", "$des")

        sendMessage(text, des)
    }

    private fun sendMessage(text: String, des: String) {

        NetworkRequest.getInstance()
            .sendMessage(NetworkRequest.SECRET_KEY, text, des)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {

                Log.i("tag","$des\n未接来电：${it.errno},${it.errmsg},${it.dataset}")
            }
    }

}