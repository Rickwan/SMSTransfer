package com.wq.smstransfer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.wq.smstransfer.net.BaseModel
import com.wq.smstransfer.net.NetworkRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


/**
 * @author wq
 * @date 2019/3/28 下午5:24
 * @desc ${TODO}
 */
class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        val datas = p1?.extras?.get("pdus") as Array<Any>

        val format = p1?.getStringExtra("format")
        val sb = StringBuilder()
        var mobile=""
        for (pdus in datas) {
            val pdusMsg = pdus as ByteArray
            val sms = SmsMessage.createFromPdu(pdusMsg, format)
            mobile = sms.originatingAddress
            sb.append(sms.messageBody)
        }

        if (sb.toString().contains("验证码")||sb.toString().contains("交易码")){
            sendMessage("来自{$mobile}新消息",sb.toString())
        }
    }


    private fun sendMessage(text: String, des: String) {

        NetworkRequest.getInstance()
            .sendMessage(NetworkRequest.SECRET_KEY,text,des)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                Log.i("tag","${it.errno},${it.errmsg},${it.dataset}")
            }

    }


}