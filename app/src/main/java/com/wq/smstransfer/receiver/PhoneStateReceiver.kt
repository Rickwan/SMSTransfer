package com.wq.smstransfer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import androidx.room.Room
import com.cheny.base.utils.SharePreHelper
import com.wq.smstransfer.db.dao.AppDatabase
import com.wq.smstransfer.db.entity.PhoneCallRecord
import com.wq.smstransfer.net.NetworkRequest
import com.wq.smstransfer.utils.PhoneUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


class PhoneStateReceiver : BroadcastReceiver() {

    private var lastCallState = TelephonyManager.CALL_STATE_IDLE

    private lateinit var format: SimpleDateFormat

    private lateinit var telephonyManager: TelephonyManager

    private lateinit var mContext: Context

    private lateinit var label: String

    override fun onReceive(context: Context?, intent: Intent?) {

        var isChecked = SharePreHelper.instance.getBooleanData(SharePreHelper.PHONE_CALL, true)

        if (!isChecked) {
            return
        }

        mContext = context!!
        format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        telephonyManager = mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
        label = format.format(Date())

        Log.i("tag", "PhoneStateReceiver:$label")

    }

    private var listener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)

            Log.i("tag", "state:$state:$label")
            if (lastCallState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE) {

//                lastCallState = TelephonyManager.CALL_STATE_IDLE
                Binder.clearCallingIdentity()
                telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE)
                queryPhoneNumber(phoneNumber!!)

            }
            lastCallState = state

        }
    }


    private fun queryPhoneNumber(phoneNumber: String) {

        var contactName = PhoneUtils.queryContact(mContext, phoneNumber)

        if (TextUtils.isEmpty(contactName)) {
            return
        }

        var date = format.format(Date(System.currentTimeMillis()))
        var text = "您有未接来电！"
        var des = "您有未接来电！，{$contactName}{$phoneNumber}于{$date}给您来电，请注意查看！"

        sendMessage(text, des)


        var isChecked = SharePreHelper.instance.getBooleanData(SharePreHelper.PHONE_CALL_SMS_FEEDBACK)

        if (isChecked) {
            sendSMS(phoneNumber)
        }

        var phoneCallRecord =
            PhoneCallRecord(phoneNumber, contactName,System.currentTimeMillis(), isChecked)

        updateSQLite(phoneCallRecord)
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

                Log.i("tag", "{$des}\n\n未接来电：${it.errno},${it.errmsg},${it.dataset}")
            }
    }


    /**
     * 给来电方发送短信
     */
    private fun sendSMS(phoneNumber: String) {

        var manager = SmsManager.getDefault()
        manager.sendTextMessage(phoneNumber!!, null, "您好，我当前不方便接听您的电话，请通过其它方式联系我。【此消息为自动回复】", null, null)
    }


    /**
     * 更新本地数据库
     */
    private fun updateSQLite(phoneCallRecord: PhoneCallRecord) {

        val db = Room.databaseBuilder(
            mContext,
            AppDatabase::class.java, "SMSTransfer"
        ).allowMainThreadQueries().build()

        db.phoneCallRecordDao().insertAll(phoneCallRecord)

    }
}

