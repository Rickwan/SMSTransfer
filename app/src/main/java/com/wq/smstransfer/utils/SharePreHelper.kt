package com.cheny.base.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils

/**
 * Created by wanqiang on 17/10/12.
 */
class SharePreHelper private constructor(){
    private var sp: SharedPreferences? = null

    private var edit: SharedPreferences.Editor? = null

    companion object {

        /**
         * 短信验证码
         */
        const val SMS_CODE = "_sms_code"

        /**
         * 未接来电
         */
        const val PHONE_CALL = "_phone_call"

        /**
         * 未接来电短信回复
         */
        const val PHONE_CALL_SMS_FEEDBACK = "_phone_call_sms_feedback"

        val instance: SharePreHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SharePreHelper() }
    }


    /**
     * 初始化

     * @param context
     * *
     * @param name
     */
     fun initialize(context: Context, name: String) {
        sp = if (TextUtils.isEmpty(name)) {
            PreferenceManager.getDefaultSharedPreferences(context)
        } else {
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }
        edit = sp!!.edit()


    }

    /**
     * 设置String数据

     * @param key
     * *
     * @param data
     */
    fun setTextData(key: String, data: String) {
        edit!!.putString(key, data)
        edit!!.commit()
    }

    /**
     * 获取String型数据

     * @param key
     * *
     * @return
     */
    fun getTextData(key: String): String {
        return sp!!.getString(key, "")
    }

    /**
     * 获取String型数据，需传默认值

     * @param key
     * *
     * @param defaultData
     * *
     * @return
     */
    fun getTextData(key: String, defaultData: String): String {
        return sp!!.getString(key, defaultData)
    }

    /**
     * 设置Boolean数据

     * @param key
     * *
     * @param data
     */
    fun setBooleanData(key: String, data: Boolean) {
        edit!!.putBoolean(key, data)
        edit!!.commit()
    }

    /**
     * 获取Boolean型数据

     * @param key
     * *
     * @return
     */
    fun getBooleanData(key: String): Boolean {
        return sp!!.getBoolean(key, false)
    }

    /**
     * 获取Boolean型数据，需传默认值

     * @param key
     * *
     * @param defaultData
     * *
     * @return
     */
    fun getBooleanData(key: String, defaultData: Boolean): Boolean {
        return sp!!.getBoolean(key, defaultData)
    }


    fun getLongData(key: String, defaultData: Long): Long {
        return sp!!.getLong(key, defaultData)
    }

    fun getLongData(key: String): Long {
        return sp!!.getLong(key, 0)
    }


    fun setLongData(key: String, data: Long) {
        edit!!.putLong(key, data)
        edit!!.commit()
    }
}