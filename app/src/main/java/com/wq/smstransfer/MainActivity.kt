package com.wq.smstransfer

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()

        toolBar.inflateMenu(R.menu.base_toolbar_menu)
        toolBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_setting) {
                var intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }

            return@setOnMenuItemClickListener true
        }

    }

    private fun requestPermissions() {

        var prxPermissions = RxPermissions(this)
        prxPermissions
            .request(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS
            )
            .subscribe {
                if (!it) {
                    showAlertDialog()
                } else {

                    var date = format.format(Date(System.currentTimeMillis()))
                    start_tv.text = "开始运行：$date"
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
            current_tv.text = "当前运行：$date"
        }
    }
}
