package com.wq.smstransfer

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import android.telephony.PhoneStateListener
import android.system.Os.listen
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager
import android.util.Log
import com.wq.smstransfer.net.NetworkRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.text.TextUtils
import com.wq.smstransfer.utils.PhoneUtils
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    private fun requestPermissions() {

        var prxPermissions = RxPermissions(this)
        prxPermissions
            .request(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS
            )
            .subscribe {
                if (!it) {
                    showAlertDialog()
                } else {
                    telephony()
                }
            }

    }

    private fun showAlertDialog() {

        var builder = AlertDialog.Builder(this)

        builder.setTitle("提示")
        builder.setMessage("您有未同意的权限！")
        builder.setPositiveButton("确定") { _, _ ->
            requestPermissions()
        }
        var dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    var lastCallState = TelephonyManager.CALL_STATE_IDLE
    /**
     * 无法判断是否为用户拒接来电还是，来电自行取消
     */
    private fun telephony() {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            tm.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {

                    if (lastCallState === TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE) {
                        Log.i("tag", "有未接来电$phoneNumber")
                        queryPhoneNumber(phoneNumber!!)
                    }
                    lastCallState = state
                    super.onCallStateChanged(state, phoneNumber)
                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        } catch (e: Exception) {
        }

    }

    private fun queryPhoneNumber(phoneNumber: String) {

        var contactName = PhoneUtils.queryContact(this, phoneNumber)

        if (TextUtils.isEmpty(contactName)) {
            return
        }
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date = format.format(Date(System.currentTimeMillis()))
        var text = "您有未接来电！"
        var des = "您有未接来电！，{$contactName}：{$phoneNumber}于{$date}给您来电，请注意查看！"
        sendMessage(text, des)
    }

    private fun sendMessage(text: String, des: String) {

        NetworkRequest.getInstance()
            .sendMessage(NetworkRequest.SECRET_KEY, text, des)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                Log.i("tag", "${it.errno},${it.errmsg},${it.dataset}")
            }

    }
}
