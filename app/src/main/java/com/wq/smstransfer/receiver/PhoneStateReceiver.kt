package com.wq.smstransfer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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


class PhoneStateReceiver : BroadcastReceiver() {

    private var lastCallState = TelephonyManager.CALL_STATE_IDLE

    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onReceive(context: Context?, intent: Intent?) {

        telephony(context!!)
    }

    private fun telephony(context: Context) {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {

            tm.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    super.onCallStateChanged(state, phoneNumber)

                    Log.i("tag","$state")
                    if (lastCallState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE) {

                        queryPhoneNumber(context, phoneNumber!!)
                        lastCallState = TelephonyManager.CALL_STATE_IDLE
                    } else {
                        lastCallState = state
                    }

                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        } catch (e: Exception) {
            lastCallState = TelephonyManager.CALL_STATE_IDLE
        }

    }


    private fun queryPhoneNumber(context: Context, phoneNumber: String) {

        var contactName = PhoneUtils.queryContact(context, phoneNumber)

        if (TextUtils.isEmpty(contactName)) {
            return
        }
        var date = format.format(Date(System.currentTimeMillis()))
        var text = "您有未接来电！"
        var des = "您有未接来电！，{$contactName}{$phoneNumber}于{$date}给您来电，请注意查看！"

        sendMessage(text, des)
    }

    private fun sendMessage(text: String, des: String) {

        NetworkRequest.getInstance()
            .sendMessage(NetworkRequest.SECRET_KEY, text, des)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {

                Log.i("tag", "{$des}\n\n未接来电：${it.errno},${it.errmsg},${it.dataset}")
            }
    }
}