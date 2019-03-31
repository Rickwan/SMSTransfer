package com.wq.smstransfer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.telephony.TelephonyManager
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log


class PhoneStateReceiver : BroadcastReceiver() {

    var lastCallState = TelephonyManager.CALL_STATE_IDLE
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val currentCallState  = telephonyManager.callState
        Log.i("tag", "currentCallState=$currentCallState ")
        if (currentCallState  == TelephonyManager.CALL_STATE_IDLE) {// 空闲
            //TODO
        } else if (currentCallState  == TelephonyManager.CALL_STATE_RINGING) {// 响铃
            //TODO
        } else if (currentCallState  == TelephonyManager.CALL_STATE_OFFHOOK) {// 接听
            //TODO
        }
        if (lastCallState  === TelephonyManager.CALL_STATE_RINGING && currentCallState == TelephonyManager.CALL_STATE_IDLE) {
            Log.i("tag", "有未接来电")
        }
        lastCallState= currentCallState

    }
}