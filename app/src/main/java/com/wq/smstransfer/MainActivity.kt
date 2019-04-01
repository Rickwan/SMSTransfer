package com.wq.smstransfer

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wq.smstransfer.net.NetworkRequest
import com.wq.smstransfer.utils.PhoneUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var startTv: TextView

    private lateinit var currentTv: TextView

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTv = findViewById(R.id.start_tv)
        currentTv = findViewById(R.id.current_tv)
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
                    var date = format.format(Date(System.currentTimeMillis()))
                    startTv.text = "开始运行：$date"
                    mHandler.sendEmptyMessage(0)
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
     * 无法判断是否为用户拒接来电，或是来电自行取消
     */
    private fun telephony() {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            tm.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {

                    if (lastCallState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE) {
                        Log.i("tag", "有未接来电:$phoneNumber")
                        queryPhoneNumber(phoneNumber!!)
                        lastCallState = TelephonyManager.CALL_STATE_IDLE
                    }
                    Log.i("tag", "lastCallState:$lastCallState,state:$state")
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
                Log.i("tag", "未接来电：${it.errno},${it.errmsg},${it.dataset}")
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeMessages(0)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private var mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            this.sendEmptyMessageDelayed(0, 1000)

            var date = format.format(Date(System.currentTimeMillis()))
            currentTv.text = "当前运行：$date"
        }
    }
}
