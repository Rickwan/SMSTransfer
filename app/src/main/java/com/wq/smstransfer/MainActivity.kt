package com.wq.smstransfer

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    private fun requestPermissions() {

        var prxPermissions = RxPermissions(this)
        prxPermissions
            .request(Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS)
            .subscribe {
                if (!it) {
                    showAlertDialog()
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
}
