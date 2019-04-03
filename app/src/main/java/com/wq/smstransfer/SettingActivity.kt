package com.wq.smstransfer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.CompoundButton
import com.cheny.base.utils.SharePreHelper
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * @author wq
 * @date 2019/4/3 下午1:40
 * @desc ${TODO}
 */
class SettingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        toolBar.setNavigationOnClickListener { finish() }

        smsCodeSwitch.isChecked = SharePreHelper.instance.getBooleanData(SharePreHelper.SMS_CODE,true)
        phoneCallSwitch.isChecked = SharePreHelper.instance.getBooleanData(SharePreHelper.PHONE_CALL,true)
        smsFeedbackSwitch.isChecked = SharePreHelper.instance.getBooleanData(SharePreHelper.PHONE_CALL_SMS_FEEDBACK)

        smsCodeSwitch.setOnCheckedChangeListener(this)
        phoneCallSwitch.setOnCheckedChangeListener(this)
        smsFeedbackSwitch.setOnCheckedChangeListener(this)
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {


        when (buttonView?.id) {
            smsCodeSwitch.id -> SharePreHelper.instance.setBooleanData(SharePreHelper.SMS_CODE, isChecked)
            phoneCallSwitch.id -> SharePreHelper.instance.setBooleanData(SharePreHelper.PHONE_CALL, isChecked)
            smsFeedbackSwitch.id -> SharePreHelper.instance.setBooleanData(
                SharePreHelper.PHONE_CALL_SMS_FEEDBACK,
                isChecked
            )
        }

    }
}