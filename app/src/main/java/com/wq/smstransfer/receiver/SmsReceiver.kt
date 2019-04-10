package com.wq.smstransfer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import androidx.room.Room
import com.cheny.base.utils.SharePreHelper
import com.wq.smstransfer.db.dao.AppDatabase
import com.wq.smstransfer.db.entity.SMSMessage
import com.wq.smstransfer.net.NetworkRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern


/**
 * @author wq
 * @date 2019/3/28 下午5:24
 * @desc ${TODO}
 */
class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context:  Context?, intent: Intent?) {


        var isChecked = SharePreHelper.instance.getBooleanData(SharePreHelper.SMS_CODE, true)

        if (!isChecked) {
            return
        }

        val datas = intent?.extras?.get("pdus") as Array<Any>

        val format = intent?.getStringExtra("format")
        val sb = StringBuilder()
        var mobile: String? = null
        for (pdus in datas) {
            val pdusMsg = pdus as ByteArray
            val sms = SmsMessage.createFromPdu(pdusMsg, format)
            mobile = sms.originatingAddress
            sb.append(sms.messageBody)
        }

        if (sb.toString().contains("验证码") || sb.toString().contains("交易码")) {

            var code = matcherCode(sb.toString())
            sendMessage("验证码:$code", sb.toString())

//            var smsMessage = SMSMessage(mobile, sb.toString(), System.currentTimeMillis())
//
//            updateSQLite(context!!, smsMessage)
        }
    }

    private fun matcherCode(body: String): String {
        val regEx = "(\\d{6}|\\d{4})"
        val pattern = Pattern.compile(regEx)
        val matcher = pattern.matcher(body)
        var code = ""
        while (matcher.find()) {
            code = matcher.group()
        }
        return code
    }


    /**
     * 推送信息
     */
    private fun sendMessage(text: String, des: String) {

        NetworkRequest.getInstance()
            .sendMessage(NetworkRequest.SECRET_KEY, text, des)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                Log.i("tag", "短信验证码：${it.errno},${it.errmsg},${it.dataset}")
            }

    }


    /**
     * 更新本地数据库
     */
    private fun updateSQLite(context: Context, smsMessage: SMSMessage) {

        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "SMSTransfer"
        ).allowMainThreadQueries().build()

        db.smsmessageDao().insertAll(smsMessage)

    }

}